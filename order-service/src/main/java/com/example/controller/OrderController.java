package com.example.controller;

import com.example.client.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private ProductClient productClient;
    
    @GetMapping("/{id}")
    public String getOrder(@PathVariable String id) {
        String product = productClient.getProduct(id);
        return "Order for " + product;
    }
}