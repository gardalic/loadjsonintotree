package com.jedox.cityexercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoadCitiesFromJsonTest {
    private final String filename = "test-json.json";

    @BeforeEach
    void setUp() throws IOException {
        createTestFile(filename);
    }

    private void createTestFile(String filename) throws IOException {
        List<Map<String, String>> cityList = new ArrayList<>(getListOfCityMaps());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(filename).toFile(), cityList);
    }

    private List<Map<String, String>> getListOfCityMaps() {
        Map<String, String> city1 = new HashMap<>();
        city1.put("country", "Madagascar");
        city1.put("city", "Antananarivo");
        city1.put("flag", "MG");
        city1.put("continent", "Africa");
        Map<String, String> city2 = new HashMap<>();
        city2.put("country", "Macedonia");
        city2.put("city", "Skopje");
        city2.put("flag", "MK");
        city2.put("continent", "Europe");
        Map<String, String> city3 = new HashMap<>();
        city3.put("country", "Cuba");
        city3.put("city", "Havana");
        city3.put("flag", "CU");
        city3.put("continent", "North America");
        Map<String, String> city4 = new HashMap<>(); // to test repeating objects
        city4.put("country", "Madagascar");
        city4.put("city", "Antananarivo");
        city4.put("flag", "MG");
        city4.put("continent", "Africa");
        Map<String, String> city5 = new HashMap<>(); // to test invalid objects
        city5.put("country", "Antananarivo");
        city5.put("city", "Madagascar");
        city5.put("flag", "MG");
        city5.put("continent", "Africa");
        Map<String, String> city6 = new HashMap<>(); // test node with two children
        city6.put("country", "Cote d'Ivoire");
        city6.put("city", "Yamoussoukro");
        city6.put("flag", "CI");
        city6.put("continent", "Africa");
        return List.of(city1, city2, city3, city4, city5, city6);
    }

    @AfterEach
    void tearDown() {
        removeTestFile(filename);
    }

    private void removeTestFile(String filename) {
        File testFile = Paths.get(filename).toFile();
        if (!testFile.delete()) {
            System.out.println("Clean up of test files failed, manual clean up required.");
        }
    }

    @Test
    void loadCitiesFromFile() {
        Map<GeoLocation, GeoLocation> testMap = new HashMap<>();
        Map<GeoLocation, GeoLocation> expectedMap = new HashMap<>();

        GeoLocation worldRoot = new Location("World");
        GeoLocation continent1 = new Location("Africa", worldRoot);
        GeoLocation continent2 = new Location("Europe", worldRoot);
        GeoLocation continent3 = new Location("North America", worldRoot);
        GeoLocation country1 = new Location("Madagascar", continent1);
        GeoLocation country2 = new Location("Macedonia", continent2);
        GeoLocation country3 = new Location("Cuba", continent3);
        GeoLocation country4 = new Location("Cote d'Ivoire", continent1);
        GeoLocation city1 = new Location("Antananarivo", country1);
        GeoLocation city2 = new Location("Skopje", country2);
        GeoLocation city3 = new Location("Havana", country3);
        GeoLocation city4 = new Location("Yamoussoukro", country4);

        expectedMap.put(continent1, worldRoot);
        expectedMap.put(continent2, worldRoot);
        expectedMap.put(continent3, worldRoot);
        expectedMap.put(country1, continent1);
        expectedMap.put(country2, continent2);
        expectedMap.put(country3, continent3);
        expectedMap.put(country4, continent1);
        expectedMap.put(city1, country1);
        expectedMap.put(city2, country2);
        expectedMap.put(city3, country3);
        expectedMap.put(city4, country4);

        LocationService locationService = new LocationService(testMap);
        LoadCitiesFromFile testFileLoader = new LoadCitiesFromJson(locationService, worldRoot);
        List<String> columnList = List.of("continent", "country", "city");
        testFileLoader.loadCitiesFromFile(filename, columnList);
        assertEquals(testMap, expectedMap);
        testFileLoader.loadCitiesFromFile(filename, columnList); // duplication test
        assertEquals(testMap, expectedMap);
    }
}