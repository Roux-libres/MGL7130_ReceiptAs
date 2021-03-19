package com.example.receiptas.model.repository;

import com.example.receiptas.model.data_model.DataMapper;
import com.example.receiptas.model.data_model.ReceiptDao;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.service.OCRService;
import com.example.receiptas.model.util.DataState;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private static final String API_KEY = "a4e5461ff488957";
    private static int OCR_ENGINE = 1;
    private static boolean IS_TABLE = false;

    private final ReceiptDao receiptDao;
    private final OCRService ocrService;
    private final DataMapper dataMapper;

    public MainRepository(ReceiptDao receiptDao, OCRService ocrService, DataMapper dataMapper) {
        this.receiptDao = receiptDao;
        this.ocrService = ocrService;
        this.dataMapper = dataMapper;
    }

    public DataState<ArrayList<String>> getParsedText(String base64Image) {
        DataState<ArrayList<String>> dataState = new DataState();
        dataState.setState(DataState.State.LOADING);

        Call<JsonObject> call = ocrService.getParsedText(API_KEY, base64Image, OCR_ENGINE, IS_TABLE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().get("ParsedText"));
                    dataState.setData(null);
                    dataState.setState(DataState.State.SUCCESS);
                } else {
                    //do nothing
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dataState.setError(t);
                dataState.setState(DataState.State.ERROR);
            }
        });

        return dataState;
    }

    public ArrayList<Receipt> getReceipts() {
        try {

        } catch (Exception e) {

        }
        return null;
    }
}
