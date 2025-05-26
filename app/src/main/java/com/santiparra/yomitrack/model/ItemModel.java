package com.santiparra.yomitrack.model;

import java.io.Serializable;

public class ItemModel implements Serializable {

    public enum ContentType {
        ANIME,
        MANGA
    }

    private final String title;
    private final String progress;
    private final String imageUrl;
    private final ContentType type;
    private final Object object;

    public ItemModel(String title, String progress, String imageUrl, ContentType type, Object object) {
        this.title = title;
        this.progress = progress;
        this.imageUrl = imageUrl;
        this.type = type;
        this.object = object;
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

    public ContentType getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
