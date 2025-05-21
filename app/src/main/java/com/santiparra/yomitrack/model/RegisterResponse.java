package com.santiparra.yomitrack.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private int userId;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}
