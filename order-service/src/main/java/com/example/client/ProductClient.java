package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.fallback.ProductServiceFallback;

@FeignClient(name = "product-service", fallback = ProductServiceFallback.class)
public interface ProductClient {

    @GetMapping("/products/{id}")
    String getProduct(@PathVariable("id") String id);
}