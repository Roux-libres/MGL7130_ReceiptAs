package com.example.receiptas.model.data_model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReceiptDao {

    private Gson gson;
    private static String filename = "/receipts.json";

    public ReceiptDao(Gson gson) {
        this.gson = gson;
    }

    public ArrayList<ReceiptDataEntity> getAll(String pathFilesDirectory) {
        ArrayList<ReceiptDataEntity> receiptDataEntities;

        try {
            JsonReader json = new JsonReader(new FileReader(pathFilesDirectory + this.filename));
            receiptDataEntities = gson.fromJson(json, new TypeToken<ArrayList<ReceiptDataEntity>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            receiptDataEntities = new ArrayList<ReceiptDataEntity>();
        }

        return receiptDataEntities;
    }

    public void setAll(String pathFilesDirectory, ArrayList<ReceiptDataEntity> entities) {
        String json = this.gson.toJson(entities);

        try {
            FileWriter fileWriter = new FileWriter(pathFilesDirectory + this.filename);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException ioException){
            System.out.println(ioException);
        }
    }
}
