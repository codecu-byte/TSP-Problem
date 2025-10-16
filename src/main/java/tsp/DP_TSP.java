package tsp;

import java.util.*;

public class DP_TSP {
    private double[][] dist;
    private int n;
    private Map<String, Double> memo;

    public List<Double> bestDistList;  // partial best distances
    public List<Integer> nfcList;      // NFC for graph

    private int nfc;

    public DP_TSP(List<City> cities) {
        this.n = cities.size();
        this.dist = new double[n][n];
        this.memo = new HashMap<>();
        this.bestDistList = new ArrayList<>();
        this.nfcList = new ArrayList<>();
        this.nfc = 0;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                dist[i][j] = cities.get(i).distanceTo(cities.get(j));
    }

    public double tsp() {
        List<Integer> nodes = new ArrayList<>();
        for (int i = 1; i < n; i++) nodes.add(i);
        double result = tspHelper(0, nodes);
        bestDistList.add(result);
        nfcList.add(nfc);
        return result;
    }

    private double tspHelper(int start, List<Integer> nodes) {
        nfc++;

        if (nodes.isEmpty()) return dist[start][0];

        String key = start + "-" + nodes.toString();
        if (memo.containsKey(key)) return memo.get(key);

        double min = Double.MAX_VALUE;
        for (int i = 0; i < nodes.size(); i++) {
            int next = nodes.get(i);
            List<Integer> remaining = new ArrayList<>(nodes);
            remaining.remove(i);
            double newDist = dist[start][next] + tspHelper(next, remaining);
            min = Math.min(min, newDist);
        }

        memo.put(key, min);

        // Record partial best distance once per subset size
        int subsetSize = nodes.size();
        if (bestDistList.size() < subsetSize + 1) {
            bestDistList.add(min);
            nfcList.add(nfc);
        }

        return min;
    }

    public int getNfc() {
        return nfc;
    }

    public List<Double> getBestDistList() {
        return bestDistList;
    }

    public List<Integer> getNfcList() {
        return nfcList;
    }
}
