package com.santiparra.yomitrack.api;

import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.AniListAnime;
import com.santiparra.yomitrack.model.LoginResponse;
import com.santiparra.yomitrack.model.RegisterResponse;

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
    Call<RegisterResponse> registerUser(@Body Map<String, String> request);
    @POST("users/login")
    Call<LoginResponse> loginUser(@Body UserEntity user);

    // ---------------- Anime ----------------
    @POST("anime/add")
    Call<String> insertAnime(@Body AnimeEntity anime);

    @GET("anime/list/{userId}")
    Call<List<AnimeEntity>> getAnimeByUser(@Path("userId") int userId);

    @PUT("anime/{id}")
    Call<String> updateAnime(@Path("id") int animeId, @Body AnimeEntity anime);

    @DELETE("anime/delete/{id}")
    Call<String> deleteAnime(@Path("id") int id);

    // ---------------- Manga ----------------
    @POST("manga/add")
    Call<String> insertManga(@Body MangaEntity manga);

    @GET("manga/list/{userId}")
    Call<List<MangaEntity>> getMangaByUser(@Path("userId") int userId);

    @PUT("manga/{id}")
    Call<String> updateManga(@Path("id") int mangaId, @Body MangaEntity manga);

    @DELETE("manga/delete/{id}")
    Call<String> deleteManga(@Path("id") int id);

    // ---------------- AniList API ----------------
    @GET("/anilist/search")
    Call<List<AniListAnime>> searchAnimeAniList(@Query("query") String query);

    @GET("anilist/search/manga")
    Call<List<AniListAnime>> searchMangaAniList(@Query("query") String query);
}
