package com.jedox.cityexercise;

import java.util.Set;

public interface GeoLocation {
    String getName();

    GeoLocation getParent();

    void setParent(GeoLocation parent);

    Set<GeoLocation> getChildren();

    void addChild(GeoLocation child);

    void logNode();
}
