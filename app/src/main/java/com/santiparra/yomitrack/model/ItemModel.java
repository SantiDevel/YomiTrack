package com.santiparra.yomitrack.model; // Ajusta el paquete si necesitas

public class ItemModel {

    private String title;
    private String progress;
    private String imageUrl;
    private ContentType contentType; // Anime o Manga

    public enum ContentType {
        ANIME,
        MANGA
    }

    // Constructor completo
    public ItemModel(String title, String progress, String imageUrl, ContentType contentType) {
        this.title = title;
        this.progress = progress;
        this.imageUrl = imageUrl;
        this.contentType = contentType;
    }

    // Getters
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

    // Setters (opcional si quieres mutar los datos después)
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
