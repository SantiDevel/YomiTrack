package com.santiparra.yomitrack.db.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Entidad que representa un usuario dentro del sistema.
 * Utilizada tanto para autenticación como para registro y obtención de datos del backend.
 */
public class UserEntity {

    /** Identificador único del usuario. */
    @SerializedName("id")
    private int id;

    /** Nombre de usuario utilizado para iniciar sesión y mostrar en el perfil. */
    @SerializedName("username")
    private String username;

    /** Contraseña del usuario (debe manejarse con cuidado y encriptación en producción). */
    @SerializedName("password")
    private String password;

    /** Dirección de correo electrónico del usuario. */
    @SerializedName("email")
    private String email;

    /**
     * Constructor utilizado para iniciar sesión con username y contraseña.
     *
     * @param username nombre de usuario.
     * @param password contraseña del usuario.
     */
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor utilizado para registrar un nuevo usuario.
     *
     * @param username nombre de usuario.
     * @param email correo electrónico.
     * @param password contraseña del usuario.
     */
    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /** @return ID del usuario. */
    public int getId() {
        return id;
    }

    /** @param id ID del usuario. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return nombre de usuario. */
    public String getUsername() {
        return username;
    }

    /** @param username nombre de usuario. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return contraseña del usuario. */
    public String getPassword() {
        return password;
    }

    /** @param password contraseña del usuario. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return correo electrónico del usuario. */
    public String getEmail() {
        return email;
    }

    /** @param email correo electrónico del usuario. */
    public void setEmail(String email) {
        this.email = email;
    }
}
