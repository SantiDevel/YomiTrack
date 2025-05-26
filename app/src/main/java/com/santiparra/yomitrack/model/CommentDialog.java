package com.santiparra.yomitrack.model;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentDialog extends Dialog {

    private final int userId;
    private final int activityId;
    private final String replyToUsername;
    private final Runnable onCommentPosted;

    private EditText editComment;
    private Button buttonSend;
    private ApiService api;

    public CommentDialog(@NonNull Context context, int userId, int activityId, Runnable onCommentPosted) {
        this(context, userId, activityId, onCommentPosted, null);
    }

    public CommentDialog(@NonNull Context context, int userId, int activityId, Runnable onCommentPosted, String replyToUsername) {
        super(context);
        this.userId = userId;
        this.activityId = activityId;
        this.replyToUsername = replyToUsername;
        this.onCommentPosted = onCommentPosted;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comment);

        editComment = findViewById(R.id.commentInput);
        buttonSend = findViewById(R.id.sendComment);
        api = ApiClient.getClient().create(ApiService.class);

        // Prefill @usuario si es respuesta
        if (replyToUsername != null && !replyToUsername.isEmpty()) {
            editComment.setText("@" + replyToUsername + " ");
            editComment.setSelection(editComment.getText().length()); // Coloca el cursor al final
        }

        buttonSend.setOnClickListener(v -> postComment());
    }

    private void postComment() {
        String text = editComment.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(getContext(), "Escribe un comentario", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObject comment = new JsonObject();
        comment.addProperty("userId", userId);
        comment.addProperty("activityId", activityId);
        comment.addProperty("text", text);

        api.postComment(comment).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    dismiss();
                    Toast.makeText(getContext(), "Comentario enviado", Toast.LENGTH_SHORT).show();
                    if (onCommentPosted != null) onCommentPosted.run();
                } else {
                    Toast.makeText(getContext(), "Error al comentar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
