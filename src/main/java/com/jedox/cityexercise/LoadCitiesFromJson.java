package com.jedox.cityexercise;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadCitiesFromJson implements LoadCitiesFromFile {
    private final LocationService locationService;
    private final GeoLocation root;

    public LoadCitiesFromJson(LocationService locationService, GeoLocation root) {
        this.locationService = locationService;
        this.root = root;
    }

    @Override
    public void loadCitiesFromFile(String filename, List<String> columnNames) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new FileInputStream(filename)) {
            // mapping to Objects because file can have a different structure than existing classes (required for direct mapping)
            Object[] cities = mapper.readValue(is, Object[].class);
            for (Object o : cities) {
                List<GeoLocation> nodes = getLocationList(columnNames, o); // column names need to be in hierarchical order
                locationService.addDistinctNodes(nodes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<GeoLocation> getLocationList(List<String> columnNames, Object o) {
        List<GeoLocation> nodes = new ArrayList<>();
        nodes.add(root);

        ObjectMapper mapper = new ObjectMapper();
        var locationInfo = mapper.convertValue(o, Map.class);
        columnNames.forEach(unit -> {
            if (locationInfo.get(unit) != null) {
                GeoLocation location = new Location(locationInfo.get(unit).toString());
                nodes.add(location);
            }
        });
        return nodes;
    }
}
