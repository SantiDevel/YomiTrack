package com.santiparra.yomitrack.ui.fragments.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

        // Espera 1.5 segundos antes de decidir a dónde ir
        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("user_id", -1);
            String username = sharedPreferences.getString("username", null);

            if (userId != -1 && username != null) {
                // Sesión activa → MainActivity
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // No hay sesión → Login
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish(); // cerrar splash
        }, 1500);
    }
}
