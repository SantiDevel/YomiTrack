package com.santiparra.yomitrack.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    /**
     * URL base del servidor donde está alojada la API del backend.
     * <p>Nota: "10.0.2.2" es una dirección especial utilizada para acceder al localhost
     * del host desde el emulador de Android.</p>
     */
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    /** Instancia única de Retrofit. */
    private static Retrofit retrofit = null;

    /**
     * Devuelve una instancia de Retrofit configurada con la URL base, un cliente HTTP
     * con un interceptor para loguear las peticiones/respuestas, y un convertidor JSON.
     *
     * @return una instancia única de Retrofit lista para usar con las interfaces de servicio.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Interceptor para mostrar logs del cuerpo de la petición y respuesta HTTP
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Cliente HTTP personalizado con el interceptor de logging
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Construcción de la instancia de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
