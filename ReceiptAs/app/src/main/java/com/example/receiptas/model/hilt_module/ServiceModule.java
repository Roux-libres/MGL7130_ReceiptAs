package com.example.receiptas.model.hilt_module;

import com.example.receiptas.model.service.OCRService;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ServiceModule {
    @Singleton
    @Provides
    public static OCRService provideOCRService(
        Gson gson
    ) {
        return new Retrofit.Builder()
                .baseUrl("https://api.ocr.space/")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(OCRService.class);
    }
}
