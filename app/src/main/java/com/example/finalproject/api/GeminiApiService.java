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

    //take api key from BuildConfig
    String API_KEY = BuildConfig.API_KEY;
    String BASE_URL = "https://generativelanguage.googleapis.com/";

    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    Call<GeminiResponse> chatWithGemini(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );

    static GeminiApiService create() {
        //create logger
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //enter to client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        //add to retrofit
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(GeminiApiService.class);
    }
}