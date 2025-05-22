package com.gunesakkaya.resthiophagame.entity;

public class Gear {
    public enum Type { SWORD, SHOES }

    public final Type type;
    public final int value; // attack bonus veya cooldown düşürme

    public Gear(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type + " +" + value;
    }
}
