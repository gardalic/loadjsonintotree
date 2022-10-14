package com.jedox.cityexercise;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Location implements GeoLocation {

    private final String name;
    private GeoLocation parent;
    private Set<GeoLocation> children;

    public Location(String name) {
        this.name = name;
    }

    public Location(String name, GeoLocation parent) {
        this.name = name;
        this.parent = parent;
        parent.addChild(this);
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
        child.setParent(this);
    }

    @Override
    public void logNode() {
        System.out.println("Node " + name + "created.");
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
}
