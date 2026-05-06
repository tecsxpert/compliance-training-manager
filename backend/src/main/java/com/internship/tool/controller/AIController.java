package com.internship.tool.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_SERVICE_URL:http://localhost:5000}")
    private String aiServiceUrl;

    @PostMapping("/describe")
    public ResponseEntity<String> describe(@RequestBody Object requestBody) {
        return proxyRequest("/api/describe", requestBody);
    }

    @PostMapping("/recommend")
    public ResponseEntity<String> recommend(@RequestBody Object requestBody) {
        return proxyRequest("/api/recommend", requestBody);
    }

    private ResponseEntity<String> proxyRequest(String endpoint, Object requestBody) {
        String url = aiServiceUrl + endpoint;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
