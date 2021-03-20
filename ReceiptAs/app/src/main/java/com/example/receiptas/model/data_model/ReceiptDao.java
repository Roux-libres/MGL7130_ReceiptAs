package com.example.receiptas.model.data_model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ReceiptDao {

    private Gson gson;
    private static String filename = "receipts.json";

    public ReceiptDao(Gson gson) {
        this.gson = gson;
    }

    public ArrayList<ReceiptDataEntity> getAll() {
        ArrayList<ReceiptDataEntity> receiptDataEntities;

        try {
            JsonReader json = new JsonReader(new FileReader(this.filename));
            receiptDataEntities = (ArrayList) this.gson.fromJson(json, Collection.class);

            return receiptDataEntities;
        } catch (FileNotFoundException fileNotFoundException){
            System.out.println(fileNotFoundException);
            receiptDataEntities = new ArrayList<ReceiptDataEntity>();
        }

        return receiptDataEntities;
    }

    public void setAll(ArrayList<ReceiptDataEntity> entities) {
        String json = this.gson.toJson(entities);

        try {
            FileWriter fileWriter = new FileWriter(this.filename);
            fileWriter.write(json);
        } catch (IOException ioException){
            System.out.println(ioException);
        }
    }
}
