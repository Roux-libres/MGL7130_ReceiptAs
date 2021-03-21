package com.example.receiptas.model.domain_model;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class Receipt {
    private String name;
    private Date date;
    private String currency;
    private ArrayList<Item> items;
    private ArrayList<Participant> participants;

    public Receipt(String name, Date date, String currency, ArrayList<Item> items, ArrayList<Participant> participants) {
        this.name = name;
        this.date = date;
        this.currency = currency;
        this.items = items;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getCurrency(){
        return currency;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }
}
