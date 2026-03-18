package com.queueless.queuelessapi.controllers;

import com.queueless.queuelessapi.models.*;
import com.queueless.queuelessapi.services.QueueService;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @PostMapping("/add")
    public Ticket addCustomer(
            @RequestParam String name,
            @RequestParam ServiceType type) {

        return queueService.addCustomer(name, type);
    }

    @GetMapping("/{type}")
    public Queue<Ticket> getQueue(@PathVariable ServiceType type) {
        return queueService.getQueue(type);
    }

    @PostMapping("/call/{type}")
    public Ticket callNext(@PathVariable ServiceType type) {
        return queueService.callNext(type);
    }

    @GetMapping("/counters")
    public List<Counter> getCounters() {
        return queueService.getCounters();
    }

    @GetMapping("/all")
    public Map<ServiceType, List<Ticket>> getAllQueues() {
        return queueService.getAllQueues();
    }

    @GetMapping("/wait-times")
    public Map<String, Integer> getWaitTimes() {
        return queueService.getEstimatedWaitTimes();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return queueService.getSystemStats();
    }

    @PostMapping("/call-counter/{counterNumber}")
    public Ticket callNextCounter(@PathVariable int counterNumber) {
        return queueService.callNextCustomer(counterNumber);
    }
}