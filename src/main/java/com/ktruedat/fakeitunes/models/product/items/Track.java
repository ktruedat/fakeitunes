package com.ktruedat.fakeitunes.models.product.items;

public class Track {
    private String name;
    private String artist;
    private String album;
    private String genre;

    public Track() {
    }

    public Track(String name, String artist, String album, String genre) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public Track setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Track setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Track setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public Track setGenre(String genre) {
        this.genre = genre;
        return this;
    }
}
