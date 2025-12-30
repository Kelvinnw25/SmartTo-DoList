package com.example.finalproject.api;

import com.example.finalproject.BuildConfig;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public interface GeminiApiService {

    // Ambil API Key dari tempat rahasia (BuildConfig)
    String API_KEY = BuildConfig.API_KEY;
    String BASE_URL = "https://generativelanguage.googleapis.com/";

    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    Call<GeminiResponse> chatWithGemini(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );

    static GeminiApiService create() {
        // 1. Bikin Logger
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 2. Masukin ke Client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        // 3. Pasang di Retrofit
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client) // <--- PENTING: Attach client di sini
                .build()
                .create(GeminiApiService.class);
    }
}