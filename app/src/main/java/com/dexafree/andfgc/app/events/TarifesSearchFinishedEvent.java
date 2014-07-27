package com.dexafree.andfgc.app.events;

import com.dexafree.andfgc.app.beans.Ticket;

import java.util.ArrayList;

public class TarifesSearchFinishedEvent {

    private ArrayList<Ticket> tickets;

    public TarifesSearchFinishedEvent(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }
}
