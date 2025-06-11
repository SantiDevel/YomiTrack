package com.santiparra.yomitrack.model;

/**
 * Modelo de datos que representa un resultado de búsqueda desde la API de AniList.
 * Utilizado tanto para anime como manga.
 */
public class AniListMedia {

    /** ID único del media (anime o manga) proporcionado por AniList. */
    private int id;

    /** Título del anime o manga. */
    private String title;

    /** URL de la imagen de portada del anime o manga. */
    private String imageUrl;

    /**
     * Constructor vacío necesario para serialización/deserialización automática.
     */
    public AniListMedia() {}

    /**
     * Devuelve el ID del media.
     *
     * @return ID del anime o manga.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del media.
     *
     * @param id ID del anime o manga.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el título del media.
     *
     * @return título del anime o manga.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título del media.
     *
     * @param title título del anime o manga.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Devuelve la URL de la imagen de portada.
     *
     * @return URL de la imagen.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Establece la URL de la imagen de portada.
     *
     * @param imageUrl URL de la imagen.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
