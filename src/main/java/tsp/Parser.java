package tsp;

import java.io.*;
import java.util.*;

public class Parser {

    public static List<City> parse(String filename) throws IOException {
        if(filename.endsWith(".csv")) {
            return parseCSV(filename);
        } else if(filename.endsWith(".tsp") || filename.endsWith(".atsp")) {
            return parseTSPLIB(filename);
        } else {
            throw new IOException("Unsupported file type: " + filename);
        }
    }

    private static List<City> parseCSV(String filename) throws IOException {
        List<City> cities = new ArrayList<>();
        InputStream is = Parser.class.getResourceAsStream("/" + filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 3) continue;
            int id = Integer.parseInt(parts[0].trim());
            double x = Double.parseDouble(parts[1].trim());
            double y = Double.parseDouble(parts[2].trim());
            cities.add(new City(id, x, y));
        }
        br.close();
        return cities;
    }

    private static List<City> parseTSPLIB(String filename) throws IOException {
        List<City> cities = new ArrayList<>();
        InputStream is = Parser.class.getResourceAsStream("/" + filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        boolean coordsSection = false;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("NODE_COORD_SECTION") || line.equals("DISPLAY_DATA_SECTION")) {
                coordsSection = true;
                continue;
            }
            if (line.equals("EOF")) break;

            if (coordsSection) {
                String[] parts = line.split("\\s+");
                if(parts.length < 3) continue;
                int id = Integer.parseInt(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                cities.add(new City(id, x, y));
            }
        }
        br.close();
        return cities;
    }
}
