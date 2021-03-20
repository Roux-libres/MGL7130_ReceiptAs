package com.example.receiptas.model.data_model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ReceiptDao {

    private Gson gson;
    private static String filename = "/receipts.json";

    public ReceiptDao(Gson gson) {
        this.gson = gson;
    }

    public ArrayList<ReceiptDataEntity> getAll(String pathFilesDirectory) {
        ArrayList<ReceiptDataEntity> receiptDataEntities;

        try {
            FileReader fileReader = new FileReader(pathFilesDirectory + this.filename);
            JsonReader json = new JsonReader(fileReader);

            receiptDataEntities = gson.fromJson(json, new TypeToken<ArrayList<ReceiptDataEntity>>(){}.getType());

            System.out.println("RECEIPT NAME : " + receiptDataEntities.get(0).getName());
            System.out.println("RECEIPT ITEMS : " + receiptDataEntities.get(0).getItems().get(0).getName() + "   for : " + receiptDataEntities.get(0).getItems().get(0).getParticipants().size());
            System.out.println("RECEIPT ITEMS : " + receiptDataEntities.get(0).getItems().get(1).getName() + "   for : " + receiptDataEntities.get(0).getItems().get(1).getParticipants().size());
            System.out.println("RECEIPT ITEMS : " + receiptDataEntities.get(0).getItems().get(2).getName() + "   for : " + receiptDataEntities.get(0).getItems().get(2).getParticipants().size());
            System.out.println("RECEIPT PARTICIPANTS : " + receiptDataEntities.get(0).getParticipants().get(0).getName());
            System.out.println("RECEIPT PARTICIPANTS : " + receiptDataEntities.get(0).getParticipants().get(1).getName());
            System.out.println("RECEIPT PARTICIPANTS : " + receiptDataEntities.get(0).getParticipants().get(2).getName());

            return receiptDataEntities;
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
