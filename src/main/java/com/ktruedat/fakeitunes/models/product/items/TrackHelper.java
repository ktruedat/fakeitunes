package com.ktruedat.fakeitunes.models.product.items;

public class TrackHelper {
    private String name;

    public TrackHelper() {
    }

    public TrackHelper(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TrackHelper setName(String name) {
        this.name = name;
        return this;
    }
}
