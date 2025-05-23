package com.gunesakkaya.resthiophagame.entity;

public class Gear {
    public enum Type { SWORD, SHOES }

    public final Type type; //Bu eşya hangi türden?
    public final int value; //Bu eşyanın sağladığı bonus değeri

    public Gear(Type type, int value) {
        this.type = type;
        this.value = value;
    }
    @Override
    public String toString() {
        return type + " +" + value; //"SWORD +2" veya "SHOES +1" gibi değer - sonrasında bunu ekranda yazdırabilirim
    }
}
