package com.example.paymentservice.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {

    public String processPayment() throws InterruptedException {

        int delay= ThreadLocalRandom.current().nextInt(1000,5001);

        System.out.println("Payment processing... delay = "+delay +" ms");
        Thread.sleep(delay);
        return "Payment Successful";
    }
}
