// RegisterFragment.java solucionado para evitar NPE en onFailure()

package com.santiparra.yomitrack.ui.fragments.register;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.model.RegisterResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private ApiService api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        usernameEditText = view.findViewById(R.id.editTextUsernameRegister);
        passwordEditText = view.findViewById(R.id.editTextPasswordRegister);
        registerButton = view.findViewById(R.id.buttonRegister);
        api = ApiClient.getClient().create(ApiService.class);

        registerButton.setOnClickListener(v -> attemptRegister());

        return view;
    }

    private void attemptRegister() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            safeToast("Todos los campos son obligatorios");
            return;
        }

        HashMap<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        api.registerUser(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().getUserId() > 0) {
                    int userId = response.body().getUserId();
                    SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                    prefs.edit().putInt("current_user_id", userId).apply();
                    safeToast("Registro exitoso");
                    requireActivity().finish();
                } else {
                    safeToast("Error al registrar usuario");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                safeToast("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    private void safeToast(String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
