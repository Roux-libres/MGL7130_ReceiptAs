package com.example.receiptas.model.data_model;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ReceiptDao {

    private Gson gson;

    public ReceiptDao(Gson gson) {
        this.gson = gson;
    }

    public ArrayList<ReceiptDataEntity> getAll() {
        return null;
    }

    public void setAll(ArrayList<ReceiptDataEntity> entities) {

    }
}
