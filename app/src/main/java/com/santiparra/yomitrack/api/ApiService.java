package com.santiparra.yomitrack.api;

import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.AniListMedia;
import com.santiparra.yomitrack.model.AnimePageResponse;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.model.LoginResponse;
import com.santiparra.yomitrack.model.MangaPageResponse;
import com.santiparra.yomitrack.model.RegisterResponse;
import com.santiparra.yomitrack.model.UserStatsResponse;
import com.santiparra.yomitrack.utils.ActivityLog;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ---------------- Usuario ----------------
    @POST("users/register")
    Call<RegisterResponse> registerUser(@Body UserEntity user);
    @POST("users/login")
    Call<LoginResponse> loginUser(@Body UserEntity user);

    // ---------------- Anime ----------------
    @POST("anime/add")
    Call<String> insertAnime(@Body AnimeEntity anime);

    @GET("anime/list/{userId}")
    Call<List<AnimeEntity>> getAnimeByUser(@Path("userId") int userId);

    // Scroll infinito: obtener lista paginada
    @GET("/anime/list/{userId}")
    Call<AnimePageResponse> getAnimes(
            @Path("userId") int userId,
            @Query("page") int page,
            @Query("size") int size
    );

    @PUT("anime/{id}")
    Call<ApiResponse> updateAnime(@Path("id") int animeId, @Body AnimeEntity anime);

    @DELETE("anime/delete/{id}")
    Call<ApiResponse> deleteAnime(@Path("id") int id);

    // ---------------- Manga ----------------
    @POST("manga/add")
    Call<String> insertManga(@Body MangaEntity manga);

    @GET("manga/list/{userId}")
    Call<List<MangaEntity>> getMangaByUser(@Path("userId") int userId);

    @GET("/manga/list/{userId}")
    Call<MangaPageResponse> getMangas(
            @Path("userId") int userId,
            @Query("page") int page,
            @Query("size") int size
    );

    @PUT("manga/{id}")
    Call<ApiResponse> updateManga(@Path("id") int mangaId, @Body MangaEntity manga);

    @DELETE("manga/delete/{id}")
    Call<ApiResponse> deleteManga(@Path("id") int id);

    // ---------------- Activity -------------------

    @GET("users/{id}/stats")
    Call<UserStatsResponse> getUserStats(@Path("id") int userId);

    @GET("api/activity/list/{userId}")
    Call<List<ActivityLog>> getActivityLog(@Path("userId") int userId);
    @POST("activity/add")
    Call<JSONObject> postActivity(@Body Map<String, Object> body);


    // ---------------- AniList API ----------------

    @GET("anilist/search")
    Call<List<AniListMedia>> searchAniList(@Query("query") String query, @Query("type") String type);

}
