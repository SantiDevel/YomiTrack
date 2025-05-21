package com.santiparra.yomitrack.model;

import com.santiparra.yomitrack.db.entities.AnimeEntity;

import java.util.List;

public class AnimePageResponse {
    private List<AnimeEntity> data;
    private int total;
    private int page;
    private boolean hasNextPage;

    public List<AnimeEntity> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}


