package com.jedox.cityexercise;

import java.util.List;
import java.util.Map;

public class LocationService {
    Map<GeoLocation, GeoLocation> locationMap;

    public LocationService(Map<GeoLocation, GeoLocation> locationMap) {
        this.locationMap = locationMap;
    }

    public boolean locationAbsent(GeoLocation location) {
        return !locationMap.containsKey(location);
    }

    public boolean isLocationValid(GeoLocation child, GeoLocation parent) {
        return locationAbsent(child) || parent.equals(locationMap.get(child));
    }

    public void addNodeToMap(GeoLocation location) {
        locationMap.put(location, location.getParent());
        location.logNode();
    }

    public boolean addDistinctNodes(List<GeoLocation> nodes, int idx) {
        if (!isLocationValid(nodes.get(idx), nodes.get(idx - 1))) {
            return false;
        }
        if (idx == nodes.size() - 1) {
            if (locationAbsent(nodes.get(idx))) {
                addNode(nodes.get(idx), nodes.get(idx - 1));
                return true;
            }
            return false; // return here after making wrapper
        }
        if (addDistinctNodes(nodes, idx + 1)) {
            if (locationAbsent(nodes.get(idx))) {
                addNode(nodes.get(idx), nodes.get(idx - 1));
            }
            return true;
        }
        return false;
    }

    private void addNode(GeoLocation child, GeoLocation parent) {
        child.setParent(parent);
        parent.addChild(child);
        addNodeToMap(child);
    }
}
