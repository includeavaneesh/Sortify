package com.sortify.main.service;
import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.model.SortifyUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// HaversineDistance class to calculate distance between two images
@Slf4j
class HaversineDistance implements DistanceMeasure {
    @Override
    public double compute(double[] a, double[] b) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(b[0] - a[0]);
        double dLng = Math.toRadians(b[1] - a[1]);
        double lat1 = Math.toRadians(a[0]);
        double lat2 = Math.toRadians(b[0]);

        double a1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a1), Math.sqrt(1 - a1));
        return earthRadius * c;
    }
}

@Slf4j
@Service
public class SortifyImageClustering {

    // Epsilon: Max radius for a cluster in mts, MIN_PTS -> minimum points to consider a cluster
    private final double EPSILON = 30000;
    private final int MIN_PTS = 1;

    @Autowired
    private SortifyUserService USER_SERVICE;
    @Autowired
    private SortifyFolderService FOLDER_SERVICE;
    @Autowired
    private SortifySubFolderService SUBFOLDER_SERVICE;
    @Autowired
    private SortifyImageService IMAGE_SERVICE;


    public void clusterImages(List<SortifyImage> imageList, String username) {
//      We set the sub folder id to 0 and delete all remaining sub folders.

        SortifyUser user = USER_SERVICE.findUserByUsername(username);
        SortifyFolder parentFolder = FOLDER_SERVICE.findFolder(user.getParentFolder().getFolderId());
        SUBFOLDER_SERVICE.deleteClusteringFolder(parentFolder.getFolderId());
        for(SortifyImage image: imageList) {
            String imageId = image.getFileName();
            IMAGE_SERVICE.updateImage(imageId, parentFolder.getFolderId() + "_0");
            log.warn("" + image);

        }


        DBSCANClusterer<SortifyImage> dbscan = new DBSCANClusterer<>(EPSILON, MIN_PTS, new HaversineDistance());
        List<Cluster<SortifyImage>> clusters = dbscan.cluster(imageList);

        updateSubFolder(clusters, username);
    }

    private void updateSubFolder(List<Cluster<SortifyImage>> clusters, String username) {

        SortifyUser user = USER_SERVICE.findUserByUsername(username);
        SortifyFolder parentFolder = FOLDER_SERVICE.findFolder(user.getParentFolder().getFolderId());
        List<SortifySubFolder> subFolderList = parentFolder.getSubFolders();

        for(int i = 0; i < clusters.size(); i++) {
            Cluster<SortifyImage> cluster = clusters.get(i);
            int clusterId = i + 1;

            SortifySubFolder newSubFolder;
            String subFolderId = parentFolder.getFolderId() + "_" + clusterId;

            if(SUBFOLDER_SERVICE.findSubFolder(subFolderId) == null) {
                newSubFolder = new SortifySubFolder();
                newSubFolder.setSubFolderId(subFolderId);
                newSubFolder.setSubFolderName(subFolderId);
                newSubFolder.setParentFolder(parentFolder);
                SUBFOLDER_SERVICE.saveSubFolder(newSubFolder);
            }
            else{
                newSubFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);
            }

            for(SortifyImage image : cluster.getPoints()) {
                String imageId = image.getFileName();
                IMAGE_SERVICE.updateImage(imageId, subFolderId);

                if(!newSubFolder.getImageList().contains(image))
                    newSubFolder.getImageList().add(image);
            }

            if(!subFolderList.contains(newSubFolder))
                subFolderList.add(newSubFolder);

            if(newSubFolder.getImageList().isEmpty()) {
                subFolderList.remove(newSubFolder);
            }


        }

        parentFolder.setSubFolders(subFolderList);
        FOLDER_SERVICE.addFolder(parentFolder);
        user.setParentFolder(parentFolder);
        USER_SERVICE.addUser(user);
    }


}
