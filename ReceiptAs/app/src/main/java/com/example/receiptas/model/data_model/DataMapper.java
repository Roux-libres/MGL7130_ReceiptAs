package com.example.receiptas.model.data_model;

import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.util.EntityMapper;

import java.util.ArrayList;

public class DataMapper implements EntityMapper<ReceiptDataEntity, Receipt> {

    @Override
    public Receipt mapFromEntity(ReceiptDataEntity receiptDataEntity) {
        return null;
    }

    @Override
    public ReceiptDataEntity mapToEntity(Receipt domainObject) {
        return null;
    }

    public ArrayList<Receipt> mapFromEntities(ArrayList<ReceiptDataEntity> entities) {
        return null;
    }

    public ArrayList<ReceiptDataEntity> mapToEntities(ArrayList<Receipt> domainObjects) {
        return null;
    }
}
