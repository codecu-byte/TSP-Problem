package tsp;

import java.util.*;

public class GA_TSP {
    private List<City> cities;
    private int populationSize;
    private double mutationRate;
    private int generations;

    public List<Integer> nfcList;      // cumulative function calls
    public List<Double> bestDistList;  // best distance per generation

    public GA_TSP(List<City> cities, int populationSize, double mutationRate, int generations) {
        this.cities = cities;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.generations = generations;
        this.nfcList = new ArrayList<>();
        this.bestDistList = new ArrayList<>();
    }

    public Tour run(double[][] distMatrix) {
        List<Tour> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) population.add(new Tour(cities));

        Tour best = population.get(0);
        int nfc = 0;

        for (int g = 0; g < generations; g++) {
            Collections.sort(population, Comparator.comparingDouble(t -> t.getDistance(distMatrix)));
            if (population.get(0).getDistance(distMatrix) < best.getDistance(distMatrix)) {
                best = population.get(0);
            }

            // track NFC and best distance
            nfc += populationSize;
            nfcList.add(nfc);
            bestDistList.add(best.getDistance(distMatrix));

            List<Tour> newPop = new ArrayList<>();
            while (newPop.size() < populationSize) {
                Tour parent1 = tournamentSelection(population, distMatrix);
                Tour parent2 = tournamentSelection(population, distMatrix);
                Tour child = crossover(parent1, parent2);
                if (Math.random() < mutationRate) child.mutate();
                newPop.add(child);
            }
            population = newPop;
        }

        return best;
    }

    private Tour tournamentSelection(List<Tour> pop, double[][] distMatrix) {
        int size = 5;
        List<Tour> tournament = new ArrayList<>();
        for (int i = 0; i < size; i++)
            tournament.add(pop.get((int)(Math.random() * pop.size())));
        return Collections.min(tournament, Comparator.comparingDouble(t -> t.getDistance(distMatrix)));
    }

    private Tour crossover(Tour p1, Tour p2) {
        List<City> child = new ArrayList<>(Collections.nCopies(cities.size(), null));
        int start = (int)(Math.random() * cities.size());
        int end = (int)(Math.random() * cities.size());

        for (int i = Math.min(start, end); i < Math.max(start, end); i++) {
            child.set(i, p1.getCities().get(i));
        }

        int current = 0;
        for (City c : p2.getCities()) {
            if (!child.contains(c)) {
                while (child.get(current) != null) current++;
                child.set(current, c);
            }
        }
        return new Tour(child, true);
    }
}
