package com.santiparra.yomitrack.ui.fragments.resetpassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.databinding.FragmentResetPasswordBinding;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;

    public ResetPasswordFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String email = binding.editEmail.getText().toString().trim();
        String token = binding.editToken.getText().toString().trim();
        String newPassword = binding.editNewPassword.getText().toString().trim();

        if (email.isEmpty() || token.isEmpty() || newPassword.isEmpty()) {
            showToast("Completa todos los campos");
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.resetPassword(email, token, newPassword).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("Contraseña restablecida correctamente");
                    NavHostFragment.findNavController(ResetPasswordFragment.this)
                            .navigate(R.id.action_resetPasswordFragment_to_loginFragment);
                } else {
                    showToast("Token inválido o expirado");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                showToast("Error de red: " + t.getMessage());
            }
        });
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
