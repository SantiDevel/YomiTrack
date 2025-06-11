package com.santiparra.yomitrack.api;

import com.google.gson.JsonObject;
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.AniListMedia;
import com.santiparra.yomitrack.model.AnimePageResponse;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.model.CommentModel;
import com.santiparra.yomitrack.model.LoginResponse;
import com.santiparra.yomitrack.model.MangaPageResponse;
import com.santiparra.yomitrack.model.RegisterResponse;
import com.santiparra.yomitrack.model.UserStatsResponse;
import com.santiparra.yomitrack.utils.ActivityLog;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Interfaz ApiService que define todos los endpoints disponibles
 * para la comunicación entre la app YomiTrack y el backend.
 *
 * <p>Usa Retrofit para declarar métodos HTTP de forma declarativa.</p>
 */
public interface ApiService {

    // -------------------- USUARIO --------------------

    /**
     * Registra un nuevo usuario en la aplicación.
     *
     * @param user Objeto con los datos del usuario.
     * @return respuesta del backend con éxito o error.
     */
    @POST("users/register")
    Call<RegisterResponse> registerUser(@Body UserEntity user);

    /**
     * Inicia sesión con un usuario existente.
     *
     * @param user Objeto con email y contraseña.
     * @return respuesta con los datos del usuario autenticado.
     */
    @POST("users/login")
    Call<LoginResponse> loginUser(@Body UserEntity user);

    /**
     * Solicita un enlace de recuperación de contraseña al correo proporcionado.
     *
     * @param email correo electrónico del usuario.
     * @return respuesta de estado de la operación.
     */
    @FormUrlEncoded
    @POST("users/forgot-password")
    Call<ApiResponse> forgotPassword(@Field("email") String email);

    /**
     * Restablece la contraseña del usuario con un token válido.
     *
     * @param email correo electrónico del usuario.
     * @param token token enviado al correo.
     * @param newPassword nueva contraseña.
     * @return respuesta de la operación.
     */
    @FormUrlEncoded
    @POST("users/reset-password")
    Call<ApiResponse> resetPassword(
            @Field("email") String email,
            @Field("token") String token,
            @Field("newPassword") String newPassword
    );

    // -------------------- ANIME --------------------

    /**
     * Inserta un nuevo anime en la lista del usuario.
     *
     * @param anime Objeto con los datos del anime.
     * @return respuesta del backend.
     */
    @POST("anime/add")
    Call<ApiResponse> insertAnime(@Body AnimeEntity anime);

    /**
     * Obtiene la lista paginada de animes de un usuario.
     *
     * @param userId ID del usuario.
     * @param page número de página.
     * @param size cantidad de ítems por página.
     * @return página con lista de animes.
     */
    @GET("/anime/list/{userId}")
    Call<AnimePageResponse> getAnimes(
            @Path("userId") int userId,
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * Actualiza un anime existente.
     *
     * @param animeId ID del anime.
     * @param anime Objeto con datos actualizados.
     * @return respuesta del backend.
     */
    @PUT("anime/{id}")
    Call<ApiResponse> updateAnime(@Path("id") int animeId, @Body AnimeEntity anime);

    /**
     * Elimina un anime por su ID.
     *
     * @param id ID del anime.
     * @return respuesta del backend.
     */
    @DELETE("anime/delete/{id}")
    Call<ApiResponse> deleteAnime(@Path("id") int id);

    /**
     * Obtiene animes por estado para un usuario específico.
     *
     * @param userId ID del usuario.
     * @param status estado deseado (e.g., Watching, Completed).
     * @return lista de animes con dicho estado.
     */
    @GET("anime/user/{userId}/status/{status}")
    Call<List<AnimeEntity>> getAnimeByUserAndStatus(
            @Path("userId") int userId,
            @Path("status") String status
    );

    // -------------------- MANGA --------------------

    /**
     * Inserta un nuevo manga en la lista del usuario.
     *
     * @param manga Objeto con los datos del manga.
     * @return respuesta del backend.
     */
    @POST("manga/add")
    Call<ApiResponse> insertManga(@Body MangaEntity manga);

    /**
     * Obtiene la lista paginada de mangas de un usuario.
     *
     * @param userId ID del usuario.
     * @param page número de página.
     * @param size cantidad de ítems por página.
     * @return página con lista de mangas.
     */
    @GET("/manga/list/{userId}")
    Call<MangaPageResponse> getMangas(
            @Path("userId") int userId,
            @Query("page") int page,
            @Query("size") int size
    );

    /**
     * Obtiene mangas por estado para un usuario específico.
     *
     * @param userId ID del usuario.
     * @param status estado deseado (e.g., Reading, Completed).
     * @return lista de mangas con dicho estado.
     */
    @GET("manga/user/{userId}/status/{status}")
    Call<List<MangaEntity>> getMangaByUserAndStatus(
            @Path("userId") int userId,
            @Path("status") String status
    );

    /**
     * Actualiza un manga existente.
     *
     * @param mangaId ID del manga.
     * @param manga Objeto con datos actualizados.
     * @return respuesta del backend.
     */
    @PUT("manga/{id}")
    Call<ApiResponse> updateManga(@Path("id") int mangaId, @Body MangaEntity manga);

    /**
     * Elimina un manga por su ID.
     *
     * @param id ID del manga.
     * @return respuesta del backend.
     */
    @DELETE("manga/delete/{id}")
    Call<ApiResponse> deleteManga(@Path("id") int id);

    // -------------------- ACTIVIDAD --------------------

    /**
     * Obtiene estadísticas de anime y manga del usuario.
     *
     * @param userId ID del usuario.
     * @return objeto con estadísticas.
     */
    @GET("users/{id}/stats")
    Call<UserStatsResponse> getUserStats(@Path("id") int userId);

    /**
     * Obtiene el historial de actividad de un usuario.
     *
     * @param userId ID del usuario.
     * @return lista de actividades.
     */
    @GET("api/activity/list/{userId}")
    Call<List<ActivityLog>> getActivityLog(@Path("userId") int userId);

    /**
     * Obtiene los comentarios de una actividad específica.
     *
     * @param activityId ID de la actividad.
     * @return lista de comentarios.
     */
    @GET("api/activity/comments/{activityId}")
    Call<List<CommentModel>> getCommentsByActivity(@Path("activityId") int activityId);

    /**
     * Verifica si un usuario dio like a una actividad.
     *
     * @param userId ID del usuario.
     * @param activityId ID de la actividad.
     * @return objeto JSON con resultado (true/false).
     */
    @GET("/api/activity/like/{userId}/{activityId}")
    Call<JsonObject> checkLike(
            @Path("userId") int userId,
            @Path("activityId") int activityId
    );

    /**
     * Envía un like a una actividad.
     *
     * @param body JSON con userId y activityId.
     * @return respuesta del backend.
     */
    @POST("api/activity/like")
    Call<JsonObject> postLike(@Body JsonObject body);

    /**
     * Elimina un like de una actividad.
     *
     * @param body JSON con userId y activityId.
     * @return respuesta del backend.
     */
    @HTTP(method = "DELETE", path = "api/activity/like/remove", hasBody = true)
    Call<JsonObject> deleteLike(@Body JsonObject body);

    /**
     * Publica un comentario en una actividad.
     *
     * @param body JSON con userId, activityId, texto y otros datos.
     * @return respuesta del backend.
     */
    @POST("api/activity/comment")
    Call<JsonObject> postComment(@Body JsonObject body);

    /**
     * Publica una nueva actividad.
     *
     * @param body mapa con los datos de la actividad (userId, tipo, contenido...).
     * @return respuesta del backend.
     */
    @POST("api/activity/add")
    Call<JsonObject> postActivity(@Body Map<String, Object> body);

    // -------------------- ANIList API --------------------

    /**
     * Busca animes o mangas en AniList.
     *
     * @param query término de búsqueda.
     * @param type tipo de media: "ANIME" o "MANGA".
     * @return lista de resultados obtenidos desde AniList.
     */
    @GET("anilist/search")
    Call<List<AniListMedia>> searchAniList(@Query("query") String query, @Query("type") String type);
}
