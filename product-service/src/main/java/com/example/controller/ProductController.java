package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.tracing.Tracer;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private Tracer tracer;

    @GetMapping("/{id}")
    public String getProduct(@PathVariable String id) {
        log.info("Getting product for ID: {}", id);
        String traceId = tracer.currentSpan().context().traceId();
        log.info("Trace ID: {}", traceId);

        String result = "Product " + id;
        log.info("Returning product information: {}", result);
        return result;
    }
}