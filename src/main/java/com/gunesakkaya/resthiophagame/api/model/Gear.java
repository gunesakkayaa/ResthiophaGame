package com.gunesakkaya.resthiophagame.api.model;

public class Gear {

    public enum Type { SWORD, SHOES }

    private Type type;
    private double value;
    private String name;

    public Gear() {}

    public Gear(Type type, double value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (+" + value + ")";
    }
}
