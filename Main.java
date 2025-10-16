package tsp;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String filename = "burma14.tsp"; 
        List<City> cities = Parser.parse(filename);
        double[][] distMatrix = buildDistanceMatrix(cities);

        // ---------------- DP ----------------
        System.out.println("=== DP TSP ===");
        DP_TSP dp = new DP_TSP(cities);
        double dpDist = dp.tsp();
        System.out.println("DP Best Distance: " + dpDist);
        System.out.println("DP NFC: " + dp.getNfc());

        // ---------------- GA initial runs ----------------
        System.out.println("\n=== GA TSP (Initial 30 Runs) ===");
        int runs = 30;
        int population = 50;
        double mutationRate = 0.05;
        int generations = 500;

        GA_TSP gaLast = null;
        double best = Double.MAX_VALUE, sum = 0, max = Double.MIN_VALUE;
        int success = 0;
        int finalGaNfc = 0;

        for (int i = 0; i < runs; i++) {
            GA_TSP ga = new GA_TSP(cities, population, mutationRate, generations);
            Tour t = ga.run(distMatrix);
            double dist = t.getDistance(distMatrix);

            System.out.println("Run " + (i + 1) + ": " + dist);

            best = Math.min(best, dist);
            max = Math.max(max, dist);
            sum += dist;
            if (dist <= dpDist) success++;

            if (i == runs - 1) {
                gaLast = ga;
                finalGaNfc = ga.nfcList.get(ga.nfcList.size() - 1);
            }
        }

        // ---------------- Graph after initial 30 runs ----------------
        plotGraph(dp, dpDist, gaLast, "DP vs GA - Distance over Normalized NFC");

        // ---------------- Summary ----------------
        double mean = sum / runs;
        System.out.println("\nGA Summary over " + runs + " runs:");
        System.out.println("Best Distance: " + best);
        System.out.println("Max Distance: " + max);
        System.out.println("Mean Distance: " + mean);
        System.out.println("Success Rate (matches DP): " + success + "/" + runs);
        System.out.println("GA Final NFC: " + finalGaNfc);

        // ---------------- Interactive extra runs ----------------
        while (true) {
            System.out.print("\nEnter additional GA runs (0 to stop): ");
            int extra = sc.nextInt();
            if (extra <= 0) break;

            double bestExtra = Double.MAX_VALUE, sumExtra = 0, maxExtra = Double.MIN_VALUE;
            int successExtra = 0;
            int finalExtraNfc = 0;
            GA_TSP gaExtraLast = null;

            for (int i = 0; i < extra; i++) {
                GA_TSP gaExtra = new GA_TSP(cities, population, mutationRate, generations);
                Tour t = gaExtra.run(distMatrix);
                double dist = t.getDistance(distMatrix);

                System.out.println("Run " + (i + 1) + ": " + dist);

                bestExtra = Math.min(bestExtra, dist);
                maxExtra = Math.max(maxExtra, dist);
                sumExtra += dist;
                if (dist <= dpDist) successExtra++;

                if (i == extra - 1) {
                    gaExtraLast = gaExtra;
                    finalExtraNfc = gaExtra.nfcList.get(gaExtra.nfcList.size() - 1);
                }
            }

            double meanExtra = sumExtra / extra;
            System.out.println("\nGA Summary over " + extra + " runs:");
            System.out.println("Best Distance: " + bestExtra);
            System.out.println("Max Distance: " + maxExtra);
            System.out.println("Mean Distance: " + meanExtra);
            System.out.println("Success Rate (matches DP): " + successExtra + "/" + extra);
            System.out.println("GA Final NFC: " + finalExtraNfc);

            // plot graph for extra runs
            plotGraph(dp, dpDist, gaExtraLast, "DP vs GA - Distance over Normalized NFC ( Extra: " + extra + ")");
        }
    }

    // ---------------- Graph plotting helper ----------------
    private static void plotGraph(DP_TSP dp, double dpOpt, GA_TSP ga, String title) {
        GraphPlotter plotter = new GraphPlotter(title);

        // DP line normalized
        List<Double> dpBest = dp.getBestDistList();
        List<Integer> dpNfc = dp.getNfcList();
        double maxDpnfc = dpNfc.get(dpNfc.size() - 1);
        double[] dpX = dpNfc.stream().mapToDouble(i -> i / maxDpnfc).toArray();
        double[] dpY = dpBest.stream().mapToDouble(d -> d).toArray();
        plotter.addSeries("DP", dpX, dpY);

        // GA line normalized
        List<Double> gaBest = ga.bestDistList;
        List<Integer> gaNfcList = ga.nfcList;
        double maxGanfc = gaNfcList.get(gaNfcList.size() - 1);
        double[] gaX = gaNfcList.stream().mapToDouble(i -> i / maxGanfc).toArray();
        double[] gaY = gaBest.stream().mapToDouble(d -> d).toArray();
        plotter.addSeries("GA", gaX, gaY);

        // DP optimal line
        double[] optX = {0, 1};
        double[] optY = {dpOpt, dpOpt};
        plotter.addSeries("DP Optimal", optX, optY);

        plotter.displayChart();
    }

    private static double[][] buildDistanceMatrix(List<City> cities) {
        int n = cities.size();
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrix[i][j] = (i == j) ? 0 : cities.get(i).distanceTo(cities.get(j));
        return matrix;
    }
}
