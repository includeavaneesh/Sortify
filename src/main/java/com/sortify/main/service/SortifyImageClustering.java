package com.sortify.main.service;
import com.sortify.main.model.SortifyFolder;
import com.sortify.main.model.SortifyImage;
import com.sortify.main.model.SortifySubFolder;
import com.sortify.main.model.SortifyUser;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// HaversineDistance class to calculate distance between two images
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

@Service
public class SortifyImageClustering {
    private final double EPSILON = 10000;
    private final int MIN_PTS = 2;

    @Autowired
    private SortifyUserService USER_SERVICE;
    @Autowired
    private SortifyFolderService FOLDER_SERVICE;
    @Autowired
    private SortifySubFolderService SUBFOLDER_SERVICE;
    @Autowired
    private SortifyImageService IMAGE_SERVICE;
    List<SortifyImage> imageList = new ArrayList<>();

    public void clusterImages(List<SortifyImage> imageList, String username) {
        /*
        todo:
        1. Extend image class to extend clusterable. (v imp)
        2. Initialize the clustering algorithm.
        3. Call clustering on the image list
        4. Update the folder
         */

        DBSCANClusterer<SortifyImage> dbscan = new DBSCANClusterer<>(EPSILON, MIN_PTS, new HaversineDistance());
        List<Cluster<SortifyImage>> clusters = dbscan.cluster(imageList);
        updateSubFolder(clusters, username);
    }

    private void updateSubFolder(List<Cluster<SortifyImage>> clusters, String username) {
        SortifyUser user = USER_SERVICE.findUserByUsername(username);
        SortifyFolder parentFolder = FOLDER_SERVICE.findFolder(user.getParentFolder().getFolderId());
        List<SortifySubFolder> subFolderList = parentFolder.getSubFolders();
        /*
        TODO:
        1. Create sub folders if they dont exist. If they exist, just add the image there.
        2. Delete any empty sub folders.
         */
        for(int i = 0; i < clusters.size(); i++) {
            Cluster<SortifyImage> cluster = clusters.get(i);
            int clusterId = i + 1;

            SortifySubFolder newSubFolder;
            String subFolderId = parentFolder.getFolderId() + "_" + clusterId;
            if(SUBFOLDER_SERVICE.findSubFolder(subFolderId) == null) {
                newSubFolder = new SortifySubFolder();
                newSubFolder.setSubFolderId(subFolderId);
                newSubFolder.setSubFolderName(subFolderId);
            }
            else{
                newSubFolder = SUBFOLDER_SERVICE.findSubFolder(subFolderId);
            }

            for(SortifyImage image : cluster.getPoints()) {
//                todo: Implement update image sub folder function
            }


        }

        parentFolder.setSubFolders(subFolderList);
        FOLDER_SERVICE.addFolder(parentFolder);
        user.setParentFolder(parentFolder);
        USER_SERVICE.addUser(user);
    }


}
