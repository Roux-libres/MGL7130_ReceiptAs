package com.example.receiptas.model.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface OCRService {
    @GET("parse/image")
    Call<JsonObject> getParsedText(
            @Header("apikey") String apiKey,
            @Body String base64Image,
            @Query("OCREngine") int ocrEngine,
            @Query("isTable") boolean isTable
    );
}
