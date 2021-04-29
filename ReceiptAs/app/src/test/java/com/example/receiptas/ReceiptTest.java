package com.example.receiptas;

import com.example.receiptas.model.domain_model.Item;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReceiptTest {

    public static final String name = "receipt";
    public static final Date date = new Date(1616282781804L);
    public static final Currency currency = Currency.getInstance(Locale.CANADA);
    public static ArrayList<Item> items;
    public static ArrayList<Participant> participants;
    private Receipt receipt;

    @Before
    public void createReceipt(){
        this.receipt = new Receipt();

        this.participants = new ArrayList<>();
        for(int i=0; i < 3; i++){
            this.participants.add(new Participant("Participant " + String.valueOf(i), false));
        }
        this.participants.get(2).setPayer(true);

        this.items = new ArrayList<>();
        for(int i=0; i < 3; i++){
            this.items.add(new Item("Item " + String.valueOf(i), i + 1, this.participants));
        }
        this.items.add(new Item("Item 4", 4, new ArrayList<>()));
    }

    @Test
    public void testSetName(){
        this.receipt.setName(this.name);

        assertEquals(this.name, this.receipt.getName());
    }

    @Test
    public void testSetDate(){
        this.receipt.setDate(this.date);

        assertEquals(this.date, this.receipt.getDate());
    }

    @Test
    public void testSetCurrency(){
        this.receipt.setCurrency(this.currency);

        assertEquals(this.currency, this.receipt.getCurrency());
    }

    @Test
    public void testSetItems(){
        this.receipt.setItems(this.items);

        assertEquals(this.items.size(), this.receipt.getItems().size());
    }

    @Test
    public void testSetParticipants(){
        this.receipt.setParticipants(this.participants);

        assertEquals(this.participants.size(), this.receipt.getParticipants().size());
    }

    @Test
    public void testGetParticipantsPayerFirst(){
        this.receipt.setParticipants(this.participants);

        assertEquals(true, this.receipt.getParticipantsPayerFirst().get(0).isPayer());
    }

    @Test
    public void testAddParticipantByName(){
        this.receipt.setParticipants(this.participants);
        String participantName = "Participant 4";

        this.receipt.addParticipantByName(participantName);
        assertEquals(participantName, this.receipt.getParticipants().get(3).getName());
    }

    @Test
    public void testRemoveParticipant(){
        this.receipt.setParticipants(this.participants);
        this.receipt.removeParticipant(this.participants.get(0));

        assertEquals(2, this.receipt.getParticipants().size());
    }

    @Test
    public void testGetUnassignedAmount(){
        this.receipt.setItems(this.items);

        assertEquals(this.items.get(3).getPrice(), this.receipt.getUnassignedAmount(), 0.01);
    }

    @Test
    public void testGetTotalAmount(){
        this.receipt.setItems(this.items);

        assertEquals(10, this.receipt.getTotalAmount(), 0.01);
    }

    @Test
    public void testGetParticipantTotal(){
        this.receipt.setParticipants(this.participants);
        this.receipt.setItems(this.items);

        assertEquals(2, this.receipt.getParticipantTotal(this.participants.get(0)), 0.01);
    }

    @Test
    public void testGetPayer(){
        this.receipt.setParticipants(this.participants);

        assertEquals(this.participants.get(2), this.receipt.getPayer());

        this.receipt.removeParticipant(this.participants.get(2));

        assertEquals(null, this.receipt.getPayer());
    }
}
