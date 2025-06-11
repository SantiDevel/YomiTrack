package com.santiparra.yomitrack.db.entities;

import java.io.Serializable;

/**
 * Entidad que representa un anime guardado por el usuario en la base de datos local.
 * Implementa Serializable para facilitar el paso entre componentes.
 */
public class AnimeEntity implements Serializable {

    /** ID único del anime en la base de datos. */
    private int id;

    /** ID del usuario al que pertenece este anime. */
    private int userId;

    /** Título del anime. */
    private String title;

    /** Puntuación asignada por el usuario (por ejemplo, del 1 al 10). */
    private int score;

    /** Progreso actual del usuario (número de episodios vistos). */
    private int progress;

    /** Estado del anime (Watching, Completed, Paused, etc.). */
    private String status;

    /** Tipo del anime (TV, Movie, OVA, etc.). */
    private String type;

    /** URL de la imagen de portada del anime. */
    private String imageUrl;

    /** Número total de episodios del anime. */
    private int totalEpisodes;

    /**
     * Constructor vacío requerido por algunas librerías como Room o Retrofit.
     */
    public AnimeEntity() {
    }

    /**
     * Constructor completo de la entidad Anime.
     *
     * @param id ID único del anime.
     * @param title Título del anime.
     * @param status Estado del anime.
     * @param userId ID del usuario propietario.
     * @param imageUrl URL de la imagen del anime.
     * @param progress Episodios vistos.
     * @param score Puntuación asignada.
     * @param totalEpisodes Total de episodios del anime.
     */
    public AnimeEntity(int id, String title, String status, int userId, String imageUrl, int progress, int score, int totalEpisodes) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.progress = progress;
        this.score = score;
        this.totalEpisodes = totalEpisodes;
    }

    /** @return ID del anime. */
    public int getId() {
        return id;
    }

    /** @param id ID del anime. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return ID del usuario. */
    public int getUserId() {
        return userId;
    }

    /** @param userId ID del usuario. */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** @return Título del anime. */
    public String getTitle() {
        return title;
    }

    /** @param title Título del anime. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return Puntuación del anime. */
    public int getScore() {
        return score;
    }

    /** @param score Puntuación del anime. */
    public void setScore(int score) {
        this.score = score;
    }

    /** @return Progreso del usuario (episodios vistos). */
    public int getProgress() {
        return progress;
    }

    /** @param progress Episodios vistos por el usuario. */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /** @return Estado del anime. */
    public String getStatus() {
        return status;
    }

    /** @param status Estado del anime. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return Tipo de anime (TV, Movie, etc.). */
    public String getType() {
        return type;
    }

    /** @param type Tipo de anime. */
    public void setType(String type) {
        this.type = type;
    }

    /** @return URL de la imagen del anime. */
    public String getImageUrl() {
        return imageUrl;
    }

    /** @param imageUrl URL de la imagen del anime. */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /** @return Total de episodios del anime. */
    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    /** @param totalEpisodes Número total de episodios. */
    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }
}
