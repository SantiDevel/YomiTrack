package com.santiparra.yomitrack.ui.fragments.forgotpassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.databinding.FragmentForgotPasswordBinding;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);

        binding.buttonSendRecovery.setOnClickListener(v -> {
            String email = binding.editEmail.getText().toString().trim();
            if (email.isEmpty()) {
                showToast("Ingresa tu correo electrónico");
                return;
            }

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            apiService.forgotPassword(email).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        showToast("Revisa tu correo para restablecer la contraseña");
                        NavHostFragment.findNavController(ForgotPasswordFragment.this)
                                .navigate(R.id.action_forgotPasswordFragment_to_resetPasswordFragment);
                    } else {
                        showToast("No se pudo enviar el correo. Verifica tu email.");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    showToast("Error: " + t.getMessage());
                }
            });
        });

        return binding.getRoot();
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
