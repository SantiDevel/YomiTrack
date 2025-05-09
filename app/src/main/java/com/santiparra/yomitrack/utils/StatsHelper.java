package com.santiparra.yomitrack.utils;

import com.santiparra.yomitrack.model.UserStats;

import java.util.ArrayList;
import java.util.List;

public class StatsHelper {

    public static List<UserStats> getAnimeStats() {
        List<UserStats> stats = new ArrayList<>();
        stats.add(new UserStats("Watching", 42, 8));
        stats.add(new UserStats("Completed", 265, 51));
        stats.add(new UserStats("On Hold", 10, 2));
        stats.add(new UserStats("Dropped", 5, 1));
        stats.add(new UserStats("Plan to Watch", 198, 38));
        return stats;
    }

    public static List<UserStats> getMangaStats() {
        List<UserStats> stats = new ArrayList<>();
        stats.add(new UserStats("Reading", 31, 10));
        stats.add(new UserStats("Completed", 121, 55));
        stats.add(new UserStats("On Hold", 5, 3));
        stats.add(new UserStats("Dropped", 4, 2));
        stats.add(new UserStats("Plan to Read", 28, 30));
        return stats;
    }
}
