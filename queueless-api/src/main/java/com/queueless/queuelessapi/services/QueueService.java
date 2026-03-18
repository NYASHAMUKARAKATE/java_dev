package com.queueless.queuelessapi.services;

import com.queueless.queuelessapi.models.Ticket;
import com.queueless.queuelessapi.models.ServiceType;
import com.queueless.queuelessapi.models.Counter;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueueService {

    private final String DATA_FILE = "queue_data.txt";
    private Map<ServiceType, Queue<Ticket>> queues = new HashMap<>();
    private List<Counter> counters = new ArrayList<>();
    private int ticketCounter = 1;
    private int totalTicketsIssued = 0;
    private int totalCustomersServed = 0;
    private int averageServiceTime = 3; // minutes per customer

    public QueueService() {

        queues.put(ServiceType.PAYMENTS, new LinkedList<>());
        queues.put(ServiceType.SUPPORT, new LinkedList<>());
        queues.put(ServiceType.ACCOUNTS, new LinkedList<>());

        counters.add(new Counter(1, ServiceType.PAYMENTS));
        counters.add(new Counter(2, ServiceType.SUPPORT));
        counters.add(new Counter(3, ServiceType.ACCOUNTS));

    }

    public Ticket addCustomer(String name, ServiceType serviceType) {

        String prefix = serviceType.name().substring(0,1);
        String ticketNumber = prefix + String.format("%03d", ticketCounter++);

        Ticket ticket = new Ticket(ticketNumber, name, serviceType);

        queues.get(serviceType).add(ticket);

        System.out.println("Ticket " + ticketNumber + " issued to " + name + " for " + serviceType + " service");

        totalTicketsIssued++;

        return ticket;
    }

    public Queue<Ticket> getQueue(ServiceType type) {
        return queues.get(type);
    }

    public Ticket callNext(ServiceType type) {
        Queue<Ticket> queue = queues.get(type);

        if (queue == null || queue.isEmpty()) {
            return null;
        }

        Ticket next = queue.poll();
        totalCustomersServed++;

        // Also update the matching counter
        for (Counter counter : counters) {
            if (counter.getServiceType() == type) {
                counter.serveTicket(next);
                break;
            }
        }

        return next;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    public Ticket callNextCustomer(int counterNumber) {

        for (Counter counter : counters) {

            if (counter.getCounterNumber() == counterNumber) {

                ServiceType type = counter.getServiceType();
                Queue<Ticket> queue = queues.get(type);

                if (queue.isEmpty()) {
                    return null; // No customers waiting
                }

                Ticket next = queue.poll();
                counter.serveTicket(next);
                totalCustomersServed++;
                
                return next;
            }
        }

        return null; // Counter not found
    }

    public Map<ServiceType, List<Ticket>> getAllQueues() {
        Map<ServiceType, List<Ticket>> allQueuesData = new HashMap<>();
        
        for (ServiceType type : queues.keySet()) {
            allQueuesData.put(type, new ArrayList<>(queues.get(type)));
        }
        
        return allQueuesData;
    }

    public Map<String, Integer> getEstimatedWaitTimes() {
        Map<String, Integer> waitTimes = new LinkedHashMap<>();

        for (ServiceType type : queues.keySet()) {
            Queue<Ticket> q = queues.get(type);
            int position = 1;

            for (Ticket t : q) {
                int waitTime = (position - 1) * averageServiceTime;
                waitTimes.put(t.getTicketNumber(), waitTime);
                position++;
            }
        }
        
        return waitTimes;
    }

    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalTicketsIssued", totalTicketsIssued);
        stats.put("customersServed", totalCustomersServed);

        int waiting = 0;
        for (Queue<Ticket> q : queues.values()) {
            waiting += q.size();
        }
        stats.put("customersWaiting", waiting);

        return stats;
    }

    public void saveQueueData() {

        try {

            java.io.PrintWriter writer = new java.io.PrintWriter(DATA_FILE);

            for (ServiceType type : queues.keySet()) {

                Queue<Ticket> q = queues.get(type);

                for (Ticket t : q) {

                    writer.println(t.getTicketNumber() + "," + t.getCustomerName() + "," + t.getServiceType());
                }
            }

            writer.close();

            System.out.println("Queue data saved.");

        } catch (Exception e) {

            System.out.println("Error saving queue data.");
        }
    }

    public void loadQueueData() {

        try {

            java.io.File file = new java.io.File(DATA_FILE);

            if (!file.exists()) {
                return;
            }

            java.util.Scanner fileScanner = new java.util.Scanner(file);

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();

                String[] parts = line.split(",");

                String ticketNumber = parts[0];
                String name = parts[1];
                ServiceType type = ServiceType.valueOf(parts[2]);

                Ticket ticket = new Ticket(ticketNumber, name, type);

                queues.get(type).add(ticket);
            }

            fileScanner.close();

            System.out.println("Queue data loaded.");

        } catch (Exception e) {

            System.out.println("Error loading queue data.");
        }
    }

}