package com.santiparra.yomitrack.model;

public class AnimeItem {
    private String title;
    private String imageUrl;
    private int watchedEpisodes;
    private int totalEpisodes;
    private double score;
    private String type;
    private String status; // Nuevo atributo

    public AnimeItem(String title, String imageUrl, int watchedEpisodes, int totalEpisodes, double score, String type, String status) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.watchedEpisodes = watchedEpisodes;
        this.totalEpisodes = totalEpisodes;
        this.score = score;
        this.type = type;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getWatchedEpisodes() {
        return watchedEpisodes;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public double getScore() {
        return score;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
