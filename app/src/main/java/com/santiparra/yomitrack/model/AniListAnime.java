package com.santiparra.yomitrack.model;

public class AniListAnime {
    private int id;
    private String title;
    private String imageUrl;

    // Constructor vacío
    public AniListAnime() {}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


