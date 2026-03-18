package com.queueless.queuelessapi.models;

public class Counter {

    private int counterNumber;
    private Ticket currentTicket;
    private ServiceType serviceType;

    public Counter(int counterNumber, ServiceType serviceType) {
        this.counterNumber = counterNumber;
        this.serviceType = serviceType;
    }

    public int getCounterNumber() {
        return counterNumber;
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public void serveTicket(Ticket ticket) {
        this.currentTicket = ticket;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }
}