package com.santiparra.yomitrack.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class UserStatsResponse {

    @SerializedName("animeStats")
    private Map<String, Integer> animeStats;

    @SerializedName("mangaStats")
    private Map<String, Integer> mangaStats;

    public Map<String, Integer> getAnimeStats() {
        return animeStats;
    }

    public Map<String, Integer> getMangaStats() {
        return mangaStats;
    }
}
