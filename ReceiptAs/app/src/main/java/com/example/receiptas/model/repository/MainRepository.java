package com.example.receiptas.model.repository;

import com.example.receiptas.model.data_model.DataMapper;
import com.example.receiptas.model.data_model.ReceiptDao;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.service.OCRService;
import com.example.receiptas.model.util.DataState;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepository {
    private static final String API_KEY = "a4e5461ff488957";
    private static int OCR_ENGINE = 2;
    private static boolean IS_TABLE = false;

    private final ReceiptDao receiptDao;
    private final OCRService ocrService;
    private final DataMapper dataMapper;

    public MainRepository(ReceiptDao receiptDao, OCRService ocrService, DataMapper dataMapper) {
        this.receiptDao = receiptDao;
        this.ocrService = ocrService;
        this.dataMapper = dataMapper;
    }

    public DataState<ArrayList<String>> getTextFromImages(ArrayList<String> images) {
        ArrayList<String> texts = new ArrayList<>();
        DataState<ArrayList<String>> dataState = new DataState();
        dataState.setLoading();

        try {
            Observable<JsonObject> request = ocrService.getParsedText(API_KEY, images.get(0), OCR_ENGINE, IS_TABLE);

            for(String image : images.subList(1, images.size())) {
                request = request.concatMapEager(result -> {
                    texts.addAll(parseJsonObjectParsedText(result));
                    return ocrService.getParsedText(API_KEY, image, OCR_ENGINE, IS_TABLE);
                });
            }
            request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                        texts.addAll(parseJsonObjectParsedText(result));
                        dataState.setSuccess(texts);
                    }, throwable -> {
                        throwable.printStackTrace();
                        dataState.setError(throwable);
                    }
                );
        } catch (Exception exception) {
            dataState.setError(exception);
        }

        return dataState;
    }

    private ArrayList<String> parseJsonObjectParsedText(JsonObject jsonObject) {
        //TODO REFACTO WITH IS TABLE TRUE
        return new ArrayList<>(
            Arrays.asList(
                jsonObject.get("ParsedResults")
                    .getAsJsonArray().get(0)
                    .getAsJsonObject().get("ParsedText")
                    .getAsString().split(OCR_ENGINE == 1 ? "\r\n" : "\n")
            )
        );
    }

    public ArrayList<Receipt> getReceipts(String pathFilesDirectory) {
        ArrayList<Receipt> receipts = new ArrayList<>();

        try {
            receipts = this.dataMapper.mapFromEntities(this.receiptDao.getAll(pathFilesDirectory));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return receipts;
    }

    public void saveReceipts(String pathFilesDirectory, ArrayList<Receipt> receipts){
        try {
            this.receiptDao.setAll(pathFilesDirectory, this.dataMapper.mapToEntities(receipts));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
