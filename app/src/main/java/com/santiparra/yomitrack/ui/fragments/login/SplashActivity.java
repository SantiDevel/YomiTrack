package com.santiparra.yomitrack.ui.fragments.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.santiparra.yomitrack.ui.MainActivity;

/**
 * Actividad inicial que decide si ir a LoginActivity o directamente a MainActivity.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Espera 1.5 segundos antes de navegar
        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (isLoggedIn) {
                // Usuario ya ha iniciado sesión previamente
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Ir a login si no ha iniciado sesión
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish();
        }, 1500); // 1.5 segundos (puedes ajustar el tiempo)
    }
}
