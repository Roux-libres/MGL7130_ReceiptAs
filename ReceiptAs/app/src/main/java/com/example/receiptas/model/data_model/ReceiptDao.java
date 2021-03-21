package com.example.receiptas.model.data_model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
