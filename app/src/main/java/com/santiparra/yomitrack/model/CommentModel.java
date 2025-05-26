package com.santiparra.yomitrack.model;

public class CommentModel {
    private int id;
    private String text;
    private boolean liked;
    private String username;
    private String avatarUrl;
    private String created_at;

    public CommentModel(String text) {
        this.text = text;
        this.liked = false;
    }

    // Constructor completo (opcional)
    public CommentModel(int id, String text, boolean liked, String username, String avatarUrl, String created_at) {
        this.id = id;
        this.text = text;
        this.liked = liked;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
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
