package com.wade.webofthings.utils.http;

import org.springframework.web.client.RestTemplate;

public class HTTPClient {

    public static String sendRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

}
