package com.santiparra.yomitrack.model;

import com.google.gson.annotations.SerializedName;
import com.santiparra.yomitrack.db.entities.UserEntity;

/**
 * Respuesta del servidor al intentar iniciar sesión.
 */
public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private UserEntity user;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserEntity getUser() {
        return user;
    }
}
