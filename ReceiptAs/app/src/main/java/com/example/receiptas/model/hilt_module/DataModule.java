package com.example.receiptas.model.hilt_module;

import com.example.receiptas.model.data_model.DataMapper;
import com.example.receiptas.model.data_model.ReceiptDao;
import com.example.receiptas.model.domain_model.Receipt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
