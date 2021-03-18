package com.example.receiptas.model.hilt_module;

import com.example.receiptas.model.data_model.DataMapper;
import com.example.receiptas.model.data_model.ReceiptDao;
import com.example.receiptas.model.repository.MainRepository;
import com.example.receiptas.model.service.OCRService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Singleton
    @Provides
    public static MainRepository provideMainRepository(
            ReceiptDao receiptDao,
            OCRService ocrService,
            DataMapper dataMapper
    ) {
        return new MainRepository(receiptDao, ocrService, dataMapper);
    }
}
