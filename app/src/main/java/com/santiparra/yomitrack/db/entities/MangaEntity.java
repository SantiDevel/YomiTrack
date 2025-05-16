package com.santiparra.yomitrack.db.entities;

import java.io.Serializable;

/**
 * Entidad que representa un manga guardado por el usuario en la base de datos local.
 */
public class MangaEntity implements Serializable {

    private int id;
    private int userId;
    private String title;
    private int score;
    private int progress;
    private String status;
    private String type;
    private String imageUrl;

    // Getters y Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public int getProgress() { return progress; }

    public void setProgress(int progress) { this.progress = progress; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
