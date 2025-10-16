package tsp;

import java.util.*;

public class Tour {
    private List<City> cities;
    private double distance = -1;
    private long nfc = 0; // Number of distance calculations

    public Tour(List<City> cities) {
        this.cities = new ArrayList<>(cities);
        Collections.shuffle(this.cities);
    }

    public Tour(List<City> cities, boolean noShuffle) {
        this.cities = new ArrayList<>(cities);
    }

    public List<City> getCities() {
        return cities;
    }

    public double getDistance(double[][] distMatrix) {
        if (distance < 0) {
            distance = 0;
            for (int i = 0; i < cities.size(); i++) {
                int from = cities.get(i).getId() - 1;
                int to = cities.get((i + 1) % cities.size()).getId() - 1;
                distance += distMatrix[from][to];
                nfc++;
            }
        }
        return distance;
    }

    public void mutate() {
        int i = (int)(cities.size() * Math.random());
        int j = (int)(cities.size() * Math.random());
        Collections.swap(cities, i, j);
        distance = -1;
        nfc = 0;
    }

    public long getNFC() {
        return nfc;
    }

    @Override
    public String toString() {
        return cities.toString();
    }
}
