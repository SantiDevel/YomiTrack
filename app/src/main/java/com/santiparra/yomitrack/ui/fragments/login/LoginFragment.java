package com.santiparra.yomitrack.ui.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.LoginResponse;
import com.santiparra.yomitrack.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento que gestiona el inicio de sesión y entrada como invitado.
 */
public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, guestButton, registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.editTextUsername);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        loginButton = view.findViewById(R.id.buttonLogin);
        guestButton = view.findViewById(R.id.buttonGuest);
        registerButton = view.findViewById(R.id.buttonGoRegister);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api = ApiClient.getClient().create(ApiService.class);
            UserEntity user = new UserEntity();
            user.username = username;
            user.password = password;

            Call<LoginResponse> call = api.loginUser(user);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse result = response.body();

                        if (result.success) {
                            UserEntity user = result.user;
                            Toast.makeText(getContext(), "Bienvenido " + user.username, Toast.LENGTH_SHORT).show();

                            saveSession(user.id); // ✅ Guardamos el ID del usuario
                            navigateToHome();     // ✅ Entramos a MainActivity
                        } else {
                            Toast.makeText(getContext(), result.message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        guestButton.setOnClickListener(v -> {
            saveSession(-1); // usuario invitado
            navigateToHome();
        });

        registerButton.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(LoginFragment.this);
            navController.navigate(R.id.action_login_to_register);
        });

        return view;
    }

    private void saveSession(int userId) {
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        prefs.edit().putInt("current_user_id", userId).apply();
    }

    private void navigateToHome() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }
}
