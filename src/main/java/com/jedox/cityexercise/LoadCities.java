package com.jedox.cityexercise;

import java.util.HashMap;
import java.util.List;

public class LoadCities {
    public static void main(String[] args) {
        LocationService locationService = new LocationService(new HashMap<>());
        String fileName = "src/main/resources/country-capitals-test.json";
        GeoLocation worldRoot = new Location("World");
        List<String> columnList = List.of("continent", "country", "city");

        LoadCitiesFromFile jsonLoader = new LoadCitiesFromJson(locationService, worldRoot);
        jsonLoader.loadCitiesFromFile(fileName, columnList);
    }
}
