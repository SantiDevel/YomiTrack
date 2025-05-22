package com.santiparra.yomitrack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de actividad reciente para mostrar acciones de usuario.
 */
public class RecentActivityModel {
    public int activityId; // ← agregar esto
    public String user;
    public String action;
    public String title;
    public String time;
    public String imageUrl;
    public List<CommentModel> comments = new ArrayList<>();

    public RecentActivityModel(int activityId, String user, String action, String title, String time, String imageUrl) {
        this.activityId = activityId;
        this.user = user;
        this.action = action;
        this.title = title;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public void addComment(CommentModel comment) {
        comments.add(comment);
    }

    public int getId() {
        return activityId;
    }
}

