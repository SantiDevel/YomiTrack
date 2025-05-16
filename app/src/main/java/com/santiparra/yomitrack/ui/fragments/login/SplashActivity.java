package com.santiparra.yomitrack.ui.fragments.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.santiparra.yomitrack.ui.MainActivity;
import com.santiparra.yomitrack.ui.fragments.login.LoginActivity;

/**
 * Actividad inicial que decide si ir a LoginActivity o directamente a MainActivity.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("current_user_id", -999);

        if (userId != -999) {
            // Ya hay una sesión guardada: entrar a la app
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // No hay sesión: ir a login
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish(); // Cierra el Splash
    }
}
