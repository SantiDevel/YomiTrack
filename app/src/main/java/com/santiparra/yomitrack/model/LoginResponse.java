package com.santiparra.yomitrack.model;

import com.santiparra.yomitrack.db.entities.UserEntity;

public class LoginResponse {
    public boolean success;
    public String message;
    public UserEntity user;
}
