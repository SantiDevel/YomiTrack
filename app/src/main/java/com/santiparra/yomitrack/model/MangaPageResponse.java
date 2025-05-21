package com.santiparra.yomitrack.model;

import com.santiparra.yomitrack.db.entities.MangaEntity;

import java.util.List;

public class MangaPageResponse {
    private List<MangaEntity> data;
    private int total;
    private int page;
    private boolean hasNextPage;

    public List<MangaEntity> getData() {
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
