package com.jedox.cityexercise;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {
    @Test
    void locationNotValid() {
        GeoLocation root = new Location("Root node");
        GeoLocation continent1 = new Location("Continent 1", root);
        GeoLocation continent2 = new Location("Continent 2", root);
        GeoLocation country1 = new Location("Country 1", continent1);
        GeoLocation country2 = new Location("Country 2", continent1);
        GeoLocation country3 = new Location("Country 3", continent1);
        GeoLocation city1 = new Location("City 1", country1);
        GeoLocation city2 = new Location("City 2", country2);
        GeoLocation city3 = new Location("City 3", country3);
        GeoLocation city4 = new Location("City 4", country3);
        root.addChild(continent1);
        root.addChild(continent2);
        continent1.addChild(country1);
        continent1.addChild(country2);
        continent2.addChild(country3);
        country1.addChild(city1);
        country2.addChild(city2);

        Map<GeoLocation, GeoLocation> nodeMap = new HashMap<>();
        nodeMap.put(root, null);
        nodeMap.put(continent1, root);
        nodeMap.put(continent2, root);
        nodeMap.put(country1, continent1);
        nodeMap.put(country2, continent1);
        nodeMap.put(country3, continent2);
        LocationService service = new LocationService(nodeMap);

        assertTrue(service.locationNotValid(country1, continent2));
        assertTrue(service.locationNotValid(country3, continent1));
        assertFalse(service.locationNotValid(city3, country1)); // city3 is valid until it is added to country3
        assertFalse(service.locationNotValid(city3, country2));
        assertTrue(service.locationNotValid(root, continent2));
        assertTrue(service.locationNotValid(continent1, city3));
        assertFalse(service.locationNotValid(city1, continent1));
        assertFalse(service.locationNotValid(city2, continent1));

        country3.addChild(city3);
        nodeMap.put(city3, country3);
        assertTrue(service.locationNotValid(city3, country1));
        assertFalse(service.locationNotValid(city3, country3));
        assertFalse(service.locationNotValid(city4, country3));
    }

    @Test
    void locationAbsent() {
        GeoLocation root = new Location("Root node");
        GeoLocation child = new Location("Child node 1", root);
        GeoLocation grandchild1 = new Location("Grandchild 1", child);
        GeoLocation grandchild2 = new Location("Grandchild 2", child);
        GeoLocation grandchild3 = new Location("Grandchild 3", child);
        root.addChild(child);
        child.addChild(grandchild1);
        child.addChild(grandchild2);

        Map<GeoLocation, GeoLocation> nodeMap = new HashMap<>();
        nodeMap.put(root, null);
        nodeMap.put(child, root);
        nodeMap.put(grandchild1, child);
        nodeMap.put(grandchild2, child);
        LocationService service = new LocationService(nodeMap);

        assertTrue(service.locationAbsent(grandchild3));
        assertFalse(service.locationAbsent(child));
        assertFalse(service.locationAbsent(grandchild1));
        assertFalse(service.locationAbsent(grandchild2));
        assertFalse(service.locationAbsent(root));

        child.addChild(grandchild3);
        nodeMap.put(grandchild3, child);
        assertFalse(service.locationAbsent(grandchild3));
    }

    @Test
    void addDistinctNodes() throws IOException {
        // setup a PrintStream to capture node logging
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream testStream = new PrintStream(outputStream);
        PrintStream originalStream = System.out;
        System.setOut(testStream);

        GeoLocation root = new Location("Root node");
        GeoLocation continent1 = new Location("Continent 1", root);
        GeoLocation continent2 = new Location("Continent 2", root);
        GeoLocation country1 = new Location("Country 1", continent1);
        GeoLocation country2 = new Location("Country 2", continent1);
        GeoLocation country3 = new Location("Country 3", continent2);
        GeoLocation city1 = new Location("City 1", country1);
        GeoLocation city2 = new Location("City 2", country2);
        GeoLocation city3 = new Location("City 3", country3);

        Map<GeoLocation, GeoLocation> expectedMap = new HashMap<>();
        Map<GeoLocation, GeoLocation> usedMap = new HashMap<>();

        LocationService service = new LocationService(usedMap);
        service.addDistinctNodes(List.of(root, continent1, country1, city1));
        service.addDistinctNodes(List.of(root, continent1, country2, city2));
        service.addDistinctNodes(List.of(root, continent2, country3, city3));
        String expectedOutput = String.join(System.lineSeparator(),
                "Node City 1 created (parent: Country 1).", // creating first city
                "Node Country 1 created (parent: Continent 1).",
                "Node Continent 1 created (parent: Root node).",
                "Node City 2 created (parent: Country 2).", // creating second city, continent node is already added
                "Node Country 2 created (parent: Continent 1).",
                "Node City 3 created (parent: Country 3).", // creating third city, new continent added
                "Node Country 3 created (parent: Continent 2).",
                "Node Continent 2 created (parent: Root node).",
                "");
        assertEquals(expectedOutput, outputStream.toString());
        service.addDistinctNodes(List.of(root, continent2, country3, city3)); // nothing should change, only new nodes are logged
        assertEquals(expectedOutput, outputStream.toString());

        expectedMap.put(continent1, root);
        expectedMap.put(country1, continent1);
        expectedMap.put(city1, country1);
        expectedMap.put(country2, continent1);
        expectedMap.put(city2, country2);
        expectedMap.put(continent2, root);
        expectedMap.put(country3, continent2);
        expectedMap.put(city3, country3);
        assertEquals(expectedMap, usedMap);

        assertEquals(continent1.getParent(), root);
        assertEquals(continent2.getParent(), root);
        assertEquals(country1.getParent(), continent1);
        assertEquals(country3.getParent(), continent2);
        assertEquals(city3.getParent(), country3);
        assertEquals(city3.getParent().getParent(), continent2);

        System.setOut(originalStream);
    }
}