package org.example.backend.utils;

import java.util.*;

public class KMeans {
    /**
     * Clusters the coordinates into two clusters.
     *
     * @param coordinates List of coordinates.
     * @return List of two clusters.
     */
    public static List<List<Float>> clusterCoordinates(List<Float> coordinates) {
        float centroid1 = Collections.min(coordinates);
        float centroid2 = Collections.max(coordinates);
        List<Float> cluster1 = new ArrayList<>();
        List<Float> cluster2 = new ArrayList<>();

        cluster1.clear();
        cluster2.clear();

        for (Float coordinate : coordinates) {
            if (Math.abs(coordinate - centroid1) < Math.abs(coordinate - centroid2)) {
                cluster1.add(coordinate);
            } else {
                cluster2.add(coordinate);
            }
        }

        return Arrays.asList(cluster1, cluster2);
    }
}
