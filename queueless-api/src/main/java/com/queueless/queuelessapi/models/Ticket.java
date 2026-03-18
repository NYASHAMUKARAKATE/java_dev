package com.queueless.queuelessapi.models;

public class Ticket {

    private String ticketNumber;
    private String customerName;
    private ServiceType serviceType;

    public Ticket(String ticketNumber, String customerName, ServiceType serviceType) {
        this.ticketNumber = ticketNumber;
        this.customerName = customerName;
        this.serviceType = serviceType;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getCustomerName() {
        return customerName;
    }
    public ServiceType getServiceType() {
        return serviceType;
    }
    
}
