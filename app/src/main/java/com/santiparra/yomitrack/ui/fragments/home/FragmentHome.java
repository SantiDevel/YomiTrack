// FragmentHome.java
package com.santiparra.yomitrack.ui.fragments.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.CommentDialog;
import com.santiparra.yomitrack.model.CommentModel;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.adapters.homeadapter.HomeCardAdapter;
import com.santiparra.yomitrack.ui.fragments.editanime.EditAnimeFragment;
import com.santiparra.yomitrack.ui.fragments.editmanga.EditMangaFragment;
import com.santiparra.yomitrack.utils.ActivityLog;
import com.santiparra.yomitrack.utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    private LinearLayout activityContainer;
    private EditText inputStatus;
    private Button btnPost;
    private RecyclerView recyclerAnime, recyclerManga;
    private ApiService api;
    private int userId;
    private String username;

    public FragmentHome() {
        super(R.layout.fragment_home);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        activityContainer = view.findViewById(R.id.activityContainer);
        inputStatus = view.findViewById(R.id.inputStatus);
        btnPost = view.findViewById(R.id.btnPost);
        recyclerAnime = view.findViewById(R.id.recyclerAnime);
        recyclerManga = view.findViewById(R.id.recyclerManga);

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);
        username = prefs.getString("username", "Usuario");

        if (userId == -1) return view;

        api = ApiClient.getClient().create(ApiService.class);

        btnPost.setOnClickListener(v -> postThought());

        loadAnimeSection();
        loadMangaSection();
        loadActivity();
        return view;
    }

    private void loadAnimeSection() {
        api.getAnimeByUserAndStatus(userId, "Watching").enqueue(new Callback<List<AnimeEntity>>() {
            @Override
            public void onResponse(Call<List<AnimeEntity>> call, Response<List<AnimeEntity>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    List<ItemModel> items = new ArrayList<>();
                    for (AnimeEntity anime : response.body()) {
                        items.add(new ItemModel(anime.getTitle(), anime.getProgress() + "/" + anime.getTotalEpisodes(), anime.getImageUrl(), ItemModel.ContentType.ANIME, anime));
                    }
                    HomeCardAdapter adapter = new HomeCardAdapter(items, item -> {
                        EditAnimeFragment fragment = new EditAnimeFragment((AnimeEntity) item.getObject());
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout, fragment)
                                .addToBackStack(null)
                                .commit();
                    });
                    recyclerAnime.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerAnime.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<AnimeEntity>> call, Throwable t) {}
        });
    }

    private void loadMangaSection() {
        api.getMangaByUserAndStatus(userId, "Reading").enqueue(new Callback<List<MangaEntity>>() {
            @Override
            public void onResponse(Call<List<MangaEntity>> call, Response<List<MangaEntity>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    List<ItemModel> items = new ArrayList<>();
                    for (MangaEntity manga : response.body()) {
                        items.add(new ItemModel(manga.getTitle(), manga.getProgress() + "/" + manga.getTotalChapters(), manga.getImageUrl(), ItemModel.ContentType.MANGA, manga));
                    }
                    HomeCardAdapter adapter = new HomeCardAdapter(items, item -> {
                        EditMangaFragment fragment = new EditMangaFragment((MangaEntity) item.getObject());
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_layout, fragment)
                                .addToBackStack(null)
                                .commit();
                    });
                    recyclerManga.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerManga.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<MangaEntity>> call, Throwable t) {}
        });
    }

    private JsonObject createLikeJson(int userId, int targetId) {
        JsonObject body = new JsonObject();
        body.addProperty("userId", userId);
        body.addProperty("activityId", targetId);
        return body;
    }

    private void actualizarCorazon(ImageButton button, boolean liked) {
        button.setImageResource(liked ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        button.setColorFilter(requireContext().getColor(liked ? R.color.pink : R.color.gray));
    }

    private void postThought() {
        String status = inputStatus.getText().toString().trim();
        if (TextUtils.isEmpty(status)) {
            Toast.makeText(getContext(), "Escribe algo", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> post = new HashMap<>();
        post.put("userId", userId);
        post.put("action", "publicó");
        post.put("mediaTitle", status);

        api.postActivity(post).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!isAdded()) return;
                if (response.isSuccessful()) {
                    inputStatus.setText("");
                    loadActivity();
                    Toast.makeText(getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadActivity() {
        api.getActivityLog(userId).enqueue(new Callback<List<ActivityLog>>() {
            @Override
            public void onResponse(Call<List<ActivityLog>> call, Response<List<ActivityLog>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    activityContainer.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(requireContext());

                    for (ActivityLog log : response.body()) {
                        View card = inflater.inflate(R.layout.item_activity_card, activityContainer, false);

                        LinearLayout commentsContainer = card.findViewById(R.id.commentsContainer);
                        commentsContainer.setVisibility(View.GONE);

                        card.setOnClickListener(v -> {
                            if (commentsContainer.getVisibility() == View.VISIBLE) {
                                commentsContainer.animate().alpha(0).setDuration(150).withEndAction(() -> commentsContainer.setVisibility(View.GONE)).start();
                            } else {
                                commentsContainer.setAlpha(0);
                                commentsContainer.setVisibility(View.VISIBLE);
                                loadComments(log.getId(), commentsContainer);
                                commentsContainer.animate().alpha(1).setDuration(150).start();
                            }
                        });

                        ((TextView) card.findViewById(R.id.activityUser)).setText(username);
                        ((TextView) card.findViewById(R.id.activityAction)).setText(log.getAction());
                        ((TextView) card.findViewById(R.id.activityTitle)).setText(log.getMediaTitle());
                        ((TextView) card.findViewById(R.id.activityTime)).setText(DateUtils.getRelativeTime(log.getTimestamp()));

                        ImageView coverImage = card.findViewById(R.id.activityCover);
                        if (!TextUtils.isEmpty(log.getImageUrl())) {
                            Glide.with(requireContext())
                                    .load(log.getImageUrl())
                                    .placeholder(R.drawable.placeholder_image)
                                    .error(R.drawable.placeholder_image)
                                    .into(coverImage);
                        }

                        ImageButton commentButton = card.findViewById(R.id.commentButton);
                        ImageButton likeButton = card.findViewById(R.id.likeButton);

                        commentButton.setOnClickListener(v -> {
                            CommentDialog dialog = new CommentDialog(requireContext(), userId, log.getId(), () -> loadComments(log.getId(), commentsContainer));
                            dialog.show();
                        });

                        api.checkLike(userId, log.getId()).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    final boolean[] isLiked = {response.body().get("liked").getAsBoolean()};
                                    actualizarCorazon(likeButton, isLiked[0]);

                                    likeButton.setOnClickListener(v -> {
                                        if (isLiked[0]) {
                                            api.deleteLike(createLikeJson(userId, log.getId())).enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if (response.isSuccessful()) {
                                                        isLiked[0] = false;
                                                        actualizarCorazon(likeButton, false);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {}
                                            });
                                        } else {
                                            api.postLike(createLikeJson(userId, log.getId())).enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                                    if (response.isSuccessful()) {
                                                        isLiked[0] = true;
                                                        actualizarCorazon(likeButton, true);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {}
                                            });
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {}
                        });

                        activityContainer.addView(card);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ActivityLog>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error al cargar actividad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComments(int activityId, LinearLayout container) {
        api.getCommentsByActivity(activityId).enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    container.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(requireContext());

                    for (CommentModel comment : response.body()) {
                        View commentView = inflater.inflate(R.layout.item_comment, container, false);

                        TextView usernameView = commentView.findViewById(R.id.commentUsername);
                        TextView commentText = commentView.findViewById(R.id.commentText);
                        TextView dateView = commentView.findViewById(R.id.commentDate);
                        ImageView avatar = commentView.findViewById(R.id.commentAvatar);
                        ImageButton likeButton = commentView.findViewById(R.id.commentLikeButton);
                        ImageButton replyButton = commentView.findViewById(R.id.replyButton);

                        usernameView.setText(comment.getUsername());
                        commentText.setText(comment.getText());
                        dateView.setText(DateUtils.getRelativeTime(comment.getCreatedAt()));

                        if (!TextUtils.isEmpty(comment.getAvatarUrl())) {
                            Glide.with(requireContext())
                                    .load(comment.getAvatarUrl())
                                    .placeholder(R.drawable.rectangle_placeholder)
                                    .error(R.drawable.error_image)
                                    .into(avatar);
                        }

                        likeButton.setImageResource(comment.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
                        likeButton.setColorFilter(requireContext().getColor(comment.isLiked() ? R.color.pink : R.color.gray));

                        likeButton.setOnClickListener(v -> {
                            boolean newLike = !comment.isLiked();
                            comment.setLiked(newLike);
                            actualizarCorazon(likeButton, newLike);
                            if (newLike) {
                                api.postLike(createLikeJson(userId, comment.getId())).enqueue(new Callback<JsonObject>() {
                                    @Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
                                    @Override public void onFailure(Call<JsonObject> call, Throwable t) {}
                                });
                            } else {
                                api.deleteLike(createLikeJson(userId, comment.getId())).enqueue(new Callback<JsonObject>() {
                                    @Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {}
                                    @Override public void onFailure(Call<JsonObject> call, Throwable t) {}
                                });
                            }
                        });

                        replyButton.setOnClickListener(v -> {
                            CommentDialog dialog = new CommentDialog(requireContext(), userId, activityId, () -> loadComments(activityId, container));
                            dialog.show();
                        });

                        container.addView(commentView);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
