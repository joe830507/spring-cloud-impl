package com.example.config;

import java.util.List;
import lombok.Data;

@Data
public class RouteDefinition {
    private String id;
    private String uri;
    private List<String> predicates;
}
