package com.example.receiptas.model.repository;

import com.example.receiptas.model.data_model.DataMapper;
import com.example.receiptas.model.data_model.ReceiptDao;
import com.example.receiptas.model.service.OCRService;

public class MainRepository {
    private ReceiptDao receiptDao;
    private OCRService ocrService;
    private DataMapper dataMapper;

    public MainRepository(ReceiptDao receiptDao, OCRService ocrService, DataMapper dataMapper) {
        this.receiptDao = receiptDao;
        this.ocrService = ocrService;
        this.dataMapper = dataMapper;
    }
}
