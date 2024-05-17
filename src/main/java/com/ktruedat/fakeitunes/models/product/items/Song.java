package com.ktruedat.fakeitunes.models.product.items;

public class Song {
    private String name;

    public Song() {
    }

    public Song(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Song setName(String name) {
        this.name = name;
        return this;
    }
}
