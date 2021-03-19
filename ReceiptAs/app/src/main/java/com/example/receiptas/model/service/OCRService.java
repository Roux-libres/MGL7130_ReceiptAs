package com.example.receiptas.model.service;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OCRService {
    @FormUrlEncoded
    @POST("parse/image")
    Observable<JsonObject> getParsedText(
            @Header("apikey") String apiKey,
            @Field("base64Image") String body,
            @Field("OCREngine") int ocrEngine,
            @Field("isTable") boolean isTable
    );
}
