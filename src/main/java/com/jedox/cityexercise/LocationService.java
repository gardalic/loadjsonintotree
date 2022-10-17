package com.jedox.cityexercise;

import java.util.List;
import java.util.Map;

public class LocationService {
    Map<GeoLocation, GeoLocation> locationMap;

    public LocationService(Map<GeoLocation, GeoLocation> locationMap) {
        this.locationMap = locationMap;
    }

    public boolean locationNotValid(GeoLocation child, GeoLocation parent) {
        return !locationAbsent(child) && !parent.equals(locationMap.get(child));
    }

    public boolean locationAbsent(GeoLocation location) {
        return !locationMap.containsKey(location);
    }

    private void addNodeToMap(GeoLocation location) {
        locationMap.put(location, location.getParent());
        location.logNode();
    }

    public void addDistinctNodes(List<GeoLocation> nodes) {
        if (locationAbsent(nodes.get(nodes.size() - 1))) {
            addNodesFromList(nodes, 1);
        }
    }

    private boolean addNodesFromList(List<GeoLocation> nodes, int idx) {
        if (locationNotValid(nodes.get(idx), nodes.get(idx - 1))) {
            return false;
        }
        if (idx == nodes.size() - 1) {
            if (locationAbsent(nodes.get(idx))) {
                addNode(nodes.get(idx), nodes.get(idx - 1));
                return true;
            }
            return false; // return here after making wrapper
        }
        if (addNodesFromList(nodes, idx + 1)) {
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
