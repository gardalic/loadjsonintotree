package com.jedox.cityexercise;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LoadCities {
    private final static String DEFAULT_FILE = "src/main/resources/country-capitals-test.json";
    private final static String[] FORMATS = new String[]{".json"};

    public static void main(String[] args) {
        LocationService locationService = new LocationService(new HashMap<>());
        String fileName = getFilenameFromArgs(args, DEFAULT_FILE);
        GeoLocation worldRoot = new Location("World");
        List<String> columnList = List.of("continent", "country", "city");

        LoadCitiesFromFile jsonLoader = new LoadCitiesFromJson(locationService, worldRoot);
        jsonLoader.loadCitiesFromFile(fileName, columnList);
    }

    public static String getFilenameFromArgs(String[] args, String defaultFile) {
        // evaluate the first argument for the file name, disregard others
        boolean hasFilename = args.length > 0 && Arrays.stream(FORMATS)
                .anyMatch(f -> f.endsWith(args[0].trim()));
        if (hasFilename) {
            File argFile = new File(args[0].trim());
            if (argFile.exists() && argFile.isFile()) {
                return argFile.getPath();
            }
        }
        return defaultFile;
    }
}
