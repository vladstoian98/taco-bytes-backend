package com.example.tacocloudapplication.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    public Map<String, Object> createPaymentIntent(Long amount) throws StripeException {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);
            Map<String, Object> data = new HashMap<>();
            data.put("clientSecret", intent.getClientSecret());

            return data;
        } catch (StripeException e) {
            System.err.println("Stripe exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create payment intent due to Stripe API error", e);
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create payment intent due to unexpected error", e);
        }
    }
}
