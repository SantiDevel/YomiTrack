package com.santiparra.yomitrack.model;

/**
 * Modelo de actividad reciente para mostrar acciones de usuario.
 */
public class RecentActivityModel {
    public String user;
    public String action;
    public String title;
    public String time;
    public String imageUrl;

    public RecentActivityModel(String user, String action, String title, String time, String imageUrl) {
        this.user = user;
        this.action = action;
        this.title = title;
        this.time = time;
        this.imageUrl = imageUrl;
    }
}
