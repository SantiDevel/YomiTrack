package com.santiparra.yomitrack.model;

public class CommentModel {
    public String text;
    public boolean liked;
    private String username;
    private String avatarUrl;
    private String created_at;


    public CommentModel(String text) {
        this.text = text;
        this.liked = false;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getCreatedAt() {
        return created_at;
    }
}
