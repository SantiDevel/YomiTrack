package com.santiparra.yomitrack.ui.fragments.register;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.databinding.FragmentRegisterBinding;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.buttonRegister.setOnClickListener(v -> registerUser());

        return binding.getRoot();
    }

    private void registerUser() {
        String username = binding.editTextUsernameRegister.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editTextPasswordRegister.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        UserEntity user = new UserEntity(username, email, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.registerUser(user).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(getContext(), "Verifica tu correo para activar la cuenta", Toast.LENGTH_LONG).show();
                    NavHostFragment.findNavController(RegisterFragment.this).popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
