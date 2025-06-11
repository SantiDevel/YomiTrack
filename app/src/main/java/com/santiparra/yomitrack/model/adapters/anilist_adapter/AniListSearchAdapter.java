package com.santiparra.yomitrack.model.adapters.anilist_adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.AniListMedia;
import com.santiparra.yomitrack.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adaptador para mostrar resultados de búsqueda provenientes de AniList
 * y permitir al usuario añadir animes o mangas a su lista local mediante la API.
 */
public class AniListSearchAdapter extends RecyclerView.Adapter<AniListSearchAdapter.ViewHolder> {

    private final List<AniListMedia> mediaList;
    private final Context context;
    private final String mediaType;

    /**
     * Constructor del adaptador.
     *
     * @param context   contexto de la aplicación.
     * @param mediaList lista de resultados de búsqueda de AniList.
     * @param mediaType tipo de media ("ANIME" o "MANGA").
     */
    public AniListSearchAdapter(Context context, List<AniListMedia> mediaList, String mediaType) {
        this.context = context;
        this.mediaList = mediaList;
        this.mediaType = mediaType;
    }

    /**
     * Infla el layout de cada ítem de la lista.
     *
     * @param parent   el ViewGroup padre.
     * @param viewType el tipo de vista.
     * @return ViewHolder que contiene la vista del ítem.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anilist_search, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asigna los datos de un ítem a su vista correspondiente.
     *
     * @param holder   ViewHolder que contiene la vista.
     * @param position posición del ítem en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AniListMedia item = mediaList.get(position);
        holder.title.setText(item.getTitle());

        Glide.with(context).load(item.getImageUrl()).into(holder.cover);

        holder.btnAdd.setOnClickListener(v -> {
            SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
            int userId = prefs.getInt("user_id", -1);

            if (userId == -1) {
                Toast.makeText(context, "Función solo disponible para usuarios registrados", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            if (mediaType.equals("ANIME")) {
                // Crear objeto AnimeEntity
                AnimeEntity anime = new AnimeEntity(
                        item.getId(),
                        item.getTitle(),
                        "Watching",
                        userId,
                        item.getImageUrl(),
                        0,
                        0,
                        0
                );
                anime.setType("TV");

                // Llamada para insertar anime
                apiService.insertAnime(anime).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Toast.makeText(context, "Anime añadido", Toast.LENGTH_SHORT).show();

                        // Registrar actividad
                        Map<String, Object> body = new HashMap<>();
                        body.put("userId", userId);
                        body.put("action", "añadió");
                        body.put("mediaTitle", item.getTitle());
                        body.put("timestamp", DateUtils.getCurrentTimestamp());
                        body.put("imageUrl", item.getImageUrl());

                        apiService.postActivity(body).enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                // Actividad registrada (sin acción adicional)
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(context, "Error al añadir anime", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // Crear objeto MangaEntity
                MangaEntity manga = new MangaEntity(
                        item.getId(),
                        item.getTitle(),
                        "Reading",
                        userId,
                        item.getImageUrl(),
                        0,
                        0,
                        0
                );
                manga.setType("Manga");

                // Llamada para insertar manga
                apiService.insertManga(manga).enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Toast.makeText(context, "Manga añadido", Toast.LENGTH_SHORT).show();

                        Map<String, Object> body = new HashMap<>();
                        body.put("userId", userId);
                        body.put("action", "añadió");
                        body.put("mediaTitle", item.getTitle());
                        body.put("timestamp", DateUtils.getCurrentTimestamp());
                        body.put("imageUrl", item.getImageUrl());

                        apiService.postActivity(body).enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call call, Response response) {}

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(context, "Error al añadir manga", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * Devuelve la cantidad de elementos en la lista.
     *
     * @return número total de ítems en la búsqueda.
     */
    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    /**
     * Clase interna que representa un ítem individual del RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** Título del anime/manga. */
        TextView title;

        /** Imagen de portada del anime/manga. */
        ImageView cover;

        /** Botón para añadir el ítem a la lista del usuario. */
        Button btnAdd;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView vista del ítem individual.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            cover = itemView.findViewById(R.id.itemImage);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
