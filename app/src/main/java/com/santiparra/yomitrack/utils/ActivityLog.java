package com.santiparra.yomitrack.utils;

import com.google.gson.annotations.SerializedName;

public class ActivityLog {

    @SerializedName("action")
    private String action;

    @SerializedName("mediaTitle")
    private String mediaTitle;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("imagenUrl")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAction() { return action; }

    public String getMediaTitle() { return mediaTitle; }

    public String getTimestamp() { return timestamp; }
}
