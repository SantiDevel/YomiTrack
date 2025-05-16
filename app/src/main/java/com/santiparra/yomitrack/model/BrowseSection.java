package com.santiparra.yomitrack.model;

import java.util.List;

/**
 * Modelo que representa una sección de la pantalla de exploración con su título y lista de ítems.
 */
public class BrowseSection {
    private final String title;
    private final List<ItemModel> items;

    public BrowseSection(String title, List<ItemModel> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<ItemModel> getItems() {
        return items;
    }
}
