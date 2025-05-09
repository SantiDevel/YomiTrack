package com.santiparra.yomitrack.model;

public class RecentActivityModel {
    public String user, action, title, time, imageUrl;

    public RecentActivityModel(String user, String action, String title, String time, String imageUrl) {
        this.user = user;
        this.action = action;
        this.title = title;
        this.time = time;
        this.imageUrl = imageUrl;
    }
}
