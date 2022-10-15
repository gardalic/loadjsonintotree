package com.jedox.cityexercise;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LoadCities {
    private static final int CONTINENT_IDX = 5;
    private static final int COUNTRY_IDX = 0;
    private static final int CITY_IDX = 1;

    public static void main(String[] args) {
        LocationService locationService = new LocationService(new HashMap<>());
        String fileName = "src/main/resources/country-capitals-test.csv";

        GeoLocation worldRoot = loadCitiesFromFile(locationService, new Location("World"), fileName);
    }

    private static GeoLocation loadCitiesFromFile(LocationService locationService, GeoLocation root, String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNext()) {
                String[] line = scanner.nextLine().split(",");
                GeoLocation continent = new Location(line[LoadCities.CONTINENT_IDX].trim(), root);
                GeoLocation country = new Location(line[LoadCities.COUNTRY_IDX].trim());
                GeoLocation city = new Location(line[LoadCities.CITY_IDX].trim());

                locationService.addDistinctNodes(List.of(root, continent, country, city), 1);
            }
        } catch (IOException e) {
            System.out.println("File could not be read: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return root;
    }
}
