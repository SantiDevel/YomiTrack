package com.santiparra.yomitrack.ui;


import android.os.Bundle;
import android.view.ViewGroup;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.databinding.ActivityMainBinding;
import com.santiparra.yomitrack.ui.fragments.anime_list.FragmentAnime;
import com.santiparra.yomitrack.ui.fragments.browse.FragmentBrowse;
import com.santiparra.yomitrack.ui.fragments.home.FragmentHome;
import com.santiparra.yomitrack.ui.fragments.manga_list.FragmentManga;
import com.santiparra.yomitrack.ui.fragments.profile.FragmentProfile;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*We initialize the values*/
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Implementation so that the toolbar does not mess with the status bar*/
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view,insets) ->{
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(0,systemBars.top,0,systemBars.bottom);
            return insets;
        });


        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView, (view, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            // Aplica padding interno para que los íconos no queden pegados al borde
            view.setPadding(0, 0, 0, bottomInset);

            // Aplica margen negativo externo para que el fondo se expanda visualmente
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) params).bottomMargin = -bottomInset;
            }

            return insets;
        });

        /*Implementation of bottomNav*/
        replaceFragment(new FragmentHome());
        //¡

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

        /*Implementation of Toolbar*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}