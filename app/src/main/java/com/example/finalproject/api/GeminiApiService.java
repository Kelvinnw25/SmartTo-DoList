package com.example.finalproject.api;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {

    // Ganti ini sama API KEY lu nanti!
    String API_KEY = "MASUKKAN_API_KEY_GEMINI_LU_DISINI";
    String BASE_URL = "https://generativelanguage.googleapis.com/";

    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    Call<GeminiResponse> chatWithGemini(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );

    // Bikin Singleton biar gampang dipanggil di mana aja
    static GeminiApiService create() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeminiApiService.class);
    }
}