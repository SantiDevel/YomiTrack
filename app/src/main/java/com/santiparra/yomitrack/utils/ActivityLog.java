package com.santiparra.yomitrack.utils;

import com.google.gson.annotations.SerializedName;

public class ActivityLog {

    @SerializedName("id")
    private int id;

    @SerializedName("userId")
    private int userId;

    @SerializedName("username")
    private String username;

    @SerializedName("action")
    private String action;

    @SerializedName("mediaTitle")
    private String mediaTitle;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("imageUrl")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getMediaTitle() {
        return mediaTitle;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
