package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.ProductClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.tracing.Tracer;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private ProductClient productClient;

    @Autowired
    private Tracer tracer;

    @GetMapping("/{id}")
    @CircuitBreaker(name = "productService", fallbackMethod = "getOrderFallback")
    public String getOrder(@PathVariable String id) {
        log.info("Getting order for ID: {}", id);
        String traceId = tracer.currentSpan().context().traceId();
        log.info("Trace ID: {}", traceId);

        String product = productClient.getProduct(id);
        log.info("Retrieved product information: {}", product);

        return "Order for " + product;
    }

    public String getOrderFallback(String id, Exception e) {
        log.warn("Fallback triggered for order ID: {}", id, e);
        return "Fallback Order for product ID: " + id;
    }
}