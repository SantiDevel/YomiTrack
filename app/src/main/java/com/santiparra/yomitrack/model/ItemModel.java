package com.santiparra.yomitrack.model;

/**
 * Modelo general de ítem para mostrar en secciones del home.
 * Se usa tanto para anime como manga.
 */
public class ItemModel {
    private String title;
    private String progress;
    private String imageUrl;
    private ContentType contentType;

    public enum ContentType {
        ANIME,
        MANGA
    }

    public ItemModel(String title, String progress, String imageUrl, ContentType contentType) {
        this.title = title;
        this.progress = progress;
        this.imageUrl = imageUrl;
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public String getProgress() {
        return progress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
