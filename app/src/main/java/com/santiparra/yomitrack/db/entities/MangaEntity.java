package com.santiparra.yomitrack.db.entities;

import java.io.Serializable;

/**
 * Entidad que representa un manga guardado por el usuario en la base de datos local.
 * Implementa Serializable para permitir su paso entre actividades y fragmentos.
 */
public class MangaEntity implements Serializable {

    /** ID único del manga en la base de datos. */
    private int id;

    /** ID del usuario al que pertenece este manga. */
    private int userId;

    /** Título del manga. */
    private String title;

    /** Puntuación asignada por el usuario (por ejemplo, del 1 al 10). */
    private int score;

    /** Progreso actual del usuario (capítulos leídos). */
    private int progress;

    /** Estado del manga (Reading, Completed, On-Hold, etc.). */
    private String status;

    /** Tipo del manga (Manga, Manhwa, Doujinshi, etc.). */
    private String type;

    /** URL de la imagen de portada del manga. */
    private String imageUrl;

    /** Número total de capítulos del manga. */
    private int totalChapters;

    /**
     * Constructor vacío necesario para serialización y frameworks como Room o Retrofit.
     */
    public MangaEntity() {
    }

    /**
     * Constructor completo para inicializar una instancia de manga.
     *
     * @param id ID único del manga.
     * @param title Título del manga.
     * @param status Estado actual del manga.
     * @param userId ID del usuario que lo añadió.
     * @param imageUrl URL de la portada del manga.
     * @param progress Capítulos leídos.
     * @param score Puntuación dada por el usuario.
     * @param totalChapters Total de capítulos disponibles.
     */
    public MangaEntity(int id, String title, String status, int userId, String imageUrl, int progress, int score, int totalChapters) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.progress = progress;
        this.score = score;
        this.totalChapters = totalChapters;
    }

    /** @return ID del manga. */
    public int getId() {
        return id;
    }

    /** @param id ID del manga. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return ID del usuario propietario del manga. */
    public int getUserId() {
        return userId;
    }

    /** @param userId ID del usuario propietario. */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** @return Título del manga. */
    public String getTitle() {
        return title;
    }

    /** @param title Título del manga. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return Puntuación asignada al manga. */
    public int getScore() {
        return score;
    }

    /** @param score Puntuación del manga. */
    public void setScore(int score) {
        this.score = score;
    }

    /** @return Capítulos leídos por el usuario. */
    public int getProgress() {
        return progress;
    }

    /** @param progress Capítulos que el usuario ha leído. */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /** @return Estado del manga (Reading, Dropped, etc.). */
    public String getStatus() {
        return status;
    }

    /** @param status Estado actual del manga. */
    public void setStatus(String status) {
        this.status = status;
    }

    /** @return Tipo de manga (e.g., Manhwa, Doujinshi). */
    public String getType() {
        return type;
    }

    /** @param type Tipo de manga. */
    public void setType(String type) {
        this.type = type;
    }

    /** @return URL de la imagen del manga. */
    public String getImageUrl() {
        return imageUrl;
    }

    /** @param imageUrl URL de la imagen de portada. */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /** @return Total de capítulos del manga. */
    public int getTotalChapters() {
        return totalChapters;
    }

    /** @param totalChapters Cantidad total de capítulos. */
    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }
}
