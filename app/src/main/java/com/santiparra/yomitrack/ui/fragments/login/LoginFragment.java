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

    public LoginFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        initListeners();
        return binding.getRoot();
    }

    private void initListeners() {
        binding.buttonLogin.setOnClickListener(v -> loginUser());
        binding.buttonGuest.setOnClickListener(v -> loginAsGuest());
        binding.buttonGoRegister.setOnClickListener(v ->
                navigateTo(R.id.action_loginFragment_to_registerFragment)
        );
        binding.textForgotPassword.setOnClickListener(v ->
                navigateTo(R.id.action_loginFragment_to_forgotPasswordFragment)
        );
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
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        saveUserSession(loginResponse.getUser().getId(), loginResponse.getUser().getUsername());
                        showToast("Inicio de sesión exitoso");
                        goToMainActivity();
                    } else {
                        showToast("Credenciales incorrectas");
                    }
                } else if (response.code() == 403) {
                    showToast("Tu correo no ha sido verificado");
                } else {
                    showToast("Error de autenticación");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                showToast("Error de red: " + t.getMessage());
            }
        });
    }

    private void loginAsGuest() {
        saveUserSession(-1, "Invitado");
        goToMainActivity();
    }

    private void saveUserSession(int userId, String username) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("user_id", userId)
                .putString("username", username)
                .apply();
    }

    private void goToMainActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }

    private void navigateTo(int destinationId) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(destinationId);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
