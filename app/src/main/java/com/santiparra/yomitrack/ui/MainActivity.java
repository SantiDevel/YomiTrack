package com.santiparra.yomitrack.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.databinding.ActivityMainBinding;
import com.santiparra.yomitrack.ui.fragments.anime_list.FragmentAnime;
import com.santiparra.yomitrack.ui.fragments.browse.FragmentBrowse;
import com.santiparra.yomitrack.ui.fragments.home.FragmentHome;
import com.santiparra.yomitrack.ui.fragments.login.LoginActivity;
import com.santiparra.yomitrack.ui.fragments.manga_list.FragmentManga;
import com.santiparra.yomitrack.ui.fragments.profile.FragmentProfile;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ajustar colores de barra de estado y navegación
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.primary));

        // Para que íconos de status bar se vean bien con fondo oscuro
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getInsetsController().setSystemBarsAppearance(0,
                    android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS |
                            android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }

        // Alinear padding según barra superior e inferior
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(0, systemBars.top, 0, 0);  // solo top, bottom lo controla el menú
            return insets;
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView, (view, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            view.setPadding(0, 0, 0, bottomInset);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) params).bottomMargin = -bottomInset;
            }
            return insets;
        });

        // Fragmento inicial
        replaceFragment(new FragmentHome());

        // Navegación inferior
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new FragmentHome());
            } else if (itemId == R.id.profile) {
                replaceFragment(new FragmentProfile());
            } else if (itemId == R.id.animelist) {
                replaceFragment(new FragmentAnime());
            } else if (itemId == R.id.mangalist) {
                replaceFragment(new FragmentManga());
            } else if (itemId == R.id.browse) {
                replaceFragment(new FragmentBrowse());
            }
            return true;
        });

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Botón de perfil
        ImageView profileIcon = findViewById(R.id.profileIconToolbar);
        profileIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Cerrar sesión")
                    .setMessage("¿Deseas cerrar sesión?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                        prefs.edit().clear().apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
