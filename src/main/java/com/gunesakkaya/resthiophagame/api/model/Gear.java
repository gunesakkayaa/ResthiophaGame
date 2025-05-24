package com.gunesakkaya.resthiophagame.api.model;

public class Gear {

    public enum Type { SWORD, SHOES }

    private Type type;
    private double value;
    private String name;

    public Gear(Type type, double value, String name) {
        this.type = type;
        this.value = value;
        this.name = name;
    }
    public int getSellValue() {
        return (int)(value * 100);
    }
    public Type getType() {
        return type;
    }
    public double getValue() {
        return value;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (+" + value + ")";
    }
}
