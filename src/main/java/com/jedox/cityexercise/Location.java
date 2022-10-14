package com.jedox.cityexercise;

import java.util.List;

public interface Location {
    Location getParent();

    List<Location> getChildren();

    void addLocation(String name, Location parent, Location child);
}
