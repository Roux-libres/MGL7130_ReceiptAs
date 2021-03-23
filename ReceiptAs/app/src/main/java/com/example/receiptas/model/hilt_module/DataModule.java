package com.example.receiptas.model.hilt_module;

import com.example.receiptas.model.data_model.DataMapper;
import com.example.receiptas.model.data_model.DateJsonDeserializer;
import com.example.receiptas.model.data_model.DateJsonSerializer;
import com.example.receiptas.model.data_model.ReceiptDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {
    @Singleton
    @Provides
    public static Gson provideGsonBuilder() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Date.class, new DateJsonSerializer())
                .registerTypeAdapter(Date.class, new DateJsonDeserializer())
                .create();
    }

    @Singleton
    @Provides
    public static ReceiptDao provideReceiptDao(
        Gson gson
    ) {
        return new ReceiptDao(gson);
    }

    @Singleton
    @Provides
    public static DataMapper provideDataMapper() {
        return new DataMapper();
    }
}
