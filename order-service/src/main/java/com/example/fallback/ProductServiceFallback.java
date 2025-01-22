package com.example.fallback;

import org.springframework.stereotype.Component;

import com.example.client.ProductClient;

@Component
public class ProductServiceFallback implements ProductClient {

    @Override
    public String getProduct(String id) {
        return "Fallback product for ID: " + id;
    }
}
