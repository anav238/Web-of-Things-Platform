package com.wade.webofthings.utils.http;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HTTPClient {

    public static String sendGetRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    public static ResponseEntity<String> sendPostRequest(String url, Map<String, Object> payload) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, payload, String.class);
    }

}
