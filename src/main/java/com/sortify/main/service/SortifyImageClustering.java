package com.sortify.main.service;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

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
    
}
