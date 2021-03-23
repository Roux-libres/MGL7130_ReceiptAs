package com.example.receiptas.model.data_model;

import com.example.receiptas.model.domain_model.Item;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.model.util.EntityMapper;

import java.util.ArrayList;

public class DataMapper implements EntityMapper<ReceiptDataEntity, Receipt> {

    @Override
    public Receipt mapFromEntity(ReceiptDataEntity receiptDataEntity) {
        ArrayList<Participant> participants = new ArrayList<Participant>();

        for(ParticipantDataEntity participantDataEntity : receiptDataEntity.getParticipants()){
            participants.add(
                    new Participant(
                            participantDataEntity.getName(),
                            participantDataEntity.isPayer()));
        }

        ArrayList<Item> items = new ArrayList<Item>();

        for(ItemDataEntity itemDataEntity : receiptDataEntity.getItems()){
            ArrayList<Participant> itemParticipants = new ArrayList<Participant>();

            for(String participantName : itemDataEntity.getParticipants()){
                for(Participant participant : participants){
                    if(participant.getName().equals(participantName)){
                        itemParticipants.add(participant);
                    }
                }
            }

            items.add(
                    new Item(
                            itemDataEntity.getName(),
                            itemDataEntity.getPrice(),
                            itemParticipants));
        }

        Receipt receipt = new Receipt(
                receiptDataEntity.getName(),
                receiptDataEntity.getDate(),
                receiptDataEntity.getCurrency(),
                items,
                participants);

        return receipt;
    }

    @Override
    public ReceiptDataEntity mapToEntity(Receipt domainObject) {
        ArrayList<ParticipantDataEntity> participantDataEntities = new ArrayList<ParticipantDataEntity>();

        for(Participant participant : domainObject.getParticipants()){
            participantDataEntities.add(
                    new ParticipantDataEntity(participant.getName(), participant.isPayer()));
        }

        ArrayList<ItemDataEntity> itemDataEntities = new ArrayList<ItemDataEntity>();

        for(Item item : domainObject.getItems()){
            ArrayList<String> participants = new ArrayList<String>();

            for(Participant participant : item.getParticipants()){
                participants.add(participant.getName());
            }

            itemDataEntities.add(
                    new ItemDataEntity(
                            item.getName(),
                            item.getPrice(),
                            participants));
        }

        ReceiptDataEntity receiptDataEntity = new ReceiptDataEntity(
                domainObject.getName(),
                domainObject.getDate(),
                domainObject.getCurrency(),
                itemDataEntities,
                participantDataEntities);

        return receiptDataEntity;
    }

    public ArrayList<Receipt> mapFromEntities(ArrayList<ReceiptDataEntity> entities) {
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();

        for(ReceiptDataEntity receiptDataEntity : entities){
            receipts.add(this.mapFromEntity(receiptDataEntity));
        }

        return receipts;
    }

    public ArrayList<ReceiptDataEntity> mapToEntities(ArrayList<Receipt> domainObjects) {
        ArrayList<ReceiptDataEntity> receiptDataEntities = new ArrayList<ReceiptDataEntity>();

        for(Receipt receipt : domainObjects){
            receiptDataEntities.add(this.mapToEntity(receipt));
        }

        return receiptDataEntities;
    }
}
