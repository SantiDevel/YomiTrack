package com.santiparra.yomitrack.ui.fragments.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.santiparra.yomitrack.R;

/**
 * Actividad contenedora del sistema de login y registro.
 * Esta actividad aloja el NavHostFragment definido en el layout activity_login.xml
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // contiene el NavHostFragment
    }
}
