package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody Long amount) {
        try {
            Map<String, Object> data = paymentService.createPaymentIntent(amount);

            return ResponseEntity.ok(data);
        } catch (StripeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
