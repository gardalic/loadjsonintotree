package com.jedox.cityexercise;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Location implements GeoLocation {

    private final String name;
    private final Set<GeoLocation> children;
    private GeoLocation parent;

    public Location(String name) {
        this.name = name != null ? name.trim() : null;
        this.children = new HashSet<>();
    }

    public Location(String name, GeoLocation parent) {
        this.name = name != null ? name.trim() : null;
        this.parent = parent;
        this.children = new HashSet<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GeoLocation getParent() {
        return parent;
    }

    @Override
    public void setParent(GeoLocation parent) {
        this.parent = parent;
    }

    @Override
    public Set<GeoLocation> getChildren() {
        return children;
    }

    @Override
    public void addChild(GeoLocation child) {
        this.children.add(child);
    }

    @Override
    public void logNode() {
        System.out.println("Node " + name + " created (parent: " + parent + ").");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;

        return Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String toString() {
        return this.name;
    }
}
