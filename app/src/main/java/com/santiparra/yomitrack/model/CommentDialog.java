package com.santiparra.yomitrack.model;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDialog extends Dialog {

    public CommentDialog(@NonNull Context context, int userId, int activityId, Runnable onCommentPosted) {
        super(context);
        setContentView(R.layout.dialog_comment);

        EditText commentInput = findViewById(R.id.commentInput);
        Button sendButton = findViewById(R.id.sendComment);

        sendButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString().trim();
            if (commentText.isEmpty()) {
                Toast.makeText(context, "Escribe un comentario", Toast.LENGTH_SHORT).show();
                return;
            }

            JsonObject body = new JsonObject();
            body.addProperty("userId", userId);
            body.addProperty("activityId", activityId);
            body.addProperty("text", commentText);

            ApiClient.getClient().create(ApiService.class).postComment(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Comentario enviado", Toast.LENGTH_SHORT).show();
                        dismiss();
                        onCommentPosted.run(); // recarga comentarios en FragmentProfile
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(context, "Error al comentar", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
