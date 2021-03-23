package com.example.receiptas.model.domain_model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class Receipt {
    private String name;
    private Date date;
    private Currency currency;
    private ArrayList<Item> items;
    private ArrayList<Participant> participants;

    public Receipt() {
        this.name = "";
        //TODO set date in scan receipt fragment/view
        this.date = new Date();
        this.currency = Currency.getInstance(Locale.getDefault());
        this.items = new ArrayList<>();
        this.participants = new ArrayList<>();
    }

    public Receipt(String name, Date date, Currency currency, ArrayList<Item> items, ArrayList<Participant> participants) {
        this.name = name;
        this.date = date;
        this.currency = currency;
        this.items = items;
        this.participants = participants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Currency getCurrency(){
        return currency;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public ArrayList<Participant> getParticipantsPayerFirst() {
        ArrayList<Participant> participants = (ArrayList<Participant>) this.getParticipants().clone();
        Participant payer = null;

        for(Participant participant : participants){
            if(participant.isPayer()){
                payer = participant;
            }
        }

        participants.remove(payer);
        //TODO refactor
        if(payer != null)
            participants.add(0, payer);

        return participants;
    }

    public void addParticipantByName(String name) {
        this.participants.add(new Participant(name, false));
    }

    public void removeParticipant(Participant participant){
        if(participants.contains(participant)) {
            for(Item item: items) {
                if(item.getParticipants().contains(participant))
                    item.getParticipants().remove(participant);
            }
            participants.remove(participant);
        }
    }

    public float getUnassignedAmount(){
        float unassignedAmount = 0;

        for(Item item : this.getItems()){
            System.out.println(item.getParticipants());
            if(item.getParticipants().size() == 0){
                System.out.println(item.getPrice());
                unassignedAmount += item.getPrice();
            }
        }

        return unassignedAmount;
    }

    public float getTotalAmount(){
        float totalAmount = 0;

        for(Item item : this.getItems()){
            totalAmount += item.getPrice();
        }

        return totalAmount;
    }

    public float getParticipantTotal(Participant participant){
        float totalAmount = 0;

        for(Item item : this.getItems()){
            if(item.getParticipants().contains(participant)){
                totalAmount += item.getPrice() / item.getParticipants().size();
            }
        }

        return totalAmount;
    }
}
