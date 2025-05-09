package com.santiparra.yomitrack.model;

public class UserStats {
    private String category;
    private int count;
    private int percentage;

    public UserStats(String category, int count, int percentage) {
        this.category = category;
        this.count = count;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public int getCount() {
        return count;
    }

    public int getPercentage() {
        return percentage;
    }
}
