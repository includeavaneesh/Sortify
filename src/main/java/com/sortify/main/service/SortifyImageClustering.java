package com.sortify.main.service;
import com.sortify.main.model.SortifyImage;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

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

public class SortifyImageClustering {
    private final double EPSILON = 10000;
    private final int MIN_PTS = 2;

    List<SortifyImage> imageList = new ArrayList<>();

    public void clusterImages(List<SortifyImage> imageList) {
        /*
        todo:
        1. Extend image class to extend clusterable. (v imp)
        2. Initialize the clustering algorithm.
        3. Call clustering on the image list
        4. Update the folder
         */

        DBSCANClusterer<SortifyImage> dbscan = new DBSCANClusterer<>(EPSILON, MIN_PTS, new HaversineDistance());
        List<Cluster<SortifyImage>> clusters = dbscan.cluster(imageList);
        updateSubFolder(clusters);
    }

    private void updateSubFolder(List<Cluster<SortifyImage>> clusters) {
        for(int i = 0; i < clusters.size(); i++) {
            Cluster<SortifyImage> cluster = clusters.get(i);
            int clusterId = i + 1;

            for(SortifyImage image : cluster.getPoints()) {
//                todo: Implement update image sub folder function
            }

        }
    }


}
