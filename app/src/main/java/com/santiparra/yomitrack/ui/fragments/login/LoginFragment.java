package com.santiparra.yomitrack.ui.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.databinding.FragmentLoginBinding;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.LoginResponse;
import com.santiparra.yomitrack.ui.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.buttonLogin.setOnClickListener(v -> loginUser());
        binding.buttonGuest.setOnClickListener(v -> loginAsGuest());
        binding.buttonGoRegister.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(LoginFragment.this);
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });

        return binding.getRoot();
    }

    private void loginUser() {
        String username = binding.editTextUsername.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Ingrese usuario y contraseña");
            return;
        }

        UserEntity user = new UserEntity(username, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.loginUser(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    int userId = response.body().getUser().getId();
                    String username = response.body().getUser().getUsername();
                    saveUserSession(userId, username, false);
                    showToast("Inicio de sesión exitoso");
                    goToMainActivity();
                } else {
                    showToast("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                showToast("Error de red: " + t.getMessage());
            }
        });
    }

    private void loginAsGuest() {
        saveUserSession(-1, "Invitado", true);
        showToast("Sesión como invitado");
        goToMainActivity();
    }

    private void saveUserSession(int userId, String username, boolean isGuest) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean("is_logged_in", true)
                .putBoolean("guest", isGuest)
                .putInt("user_id", userId)
                .putString("username", username)
                .apply();
    }

    private void goToMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
