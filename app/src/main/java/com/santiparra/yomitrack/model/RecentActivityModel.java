package com.santiparra.yomitrack.model;

import java.util.List;

public class RecentActivityModel {

    private final int id;
    private final int userId;
    private final String user;
    public final String action;
    public final String title;
    public final String imageUrl;
    public final String time;
    public final List<CommentModel> comments;
    public boolean liked;

    public RecentActivityModel(int id, int userId, String user, String action, String title, String imageUrl, String time, List<CommentModel> comments, boolean liked) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.action = action;
        this.title = title;
        this.imageUrl = imageUrl;
        this.time = time;
        this.comments = comments;
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUser() {
        return user;
    }
}
