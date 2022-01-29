package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.utils.http.HTTPClient;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceProperty {
    private String type;
    private String name;
    private String title;
    private String currentValue;
    private String minimum;
    private String maximum;
    private String unit;
    private String description;

    public DeviceProperty() {
    }

    public void updateCurrentValue(String baseLink, ObjectMapper objectMapper) {
        if (baseLink == null)
            return;
        try {
            String response = HTTPClient.sendRequest(baseLink + "/properties/" + name);
            Map<String, String> propertyJson = objectMapper.readValue(response,  new TypeReference<>(){});
            currentValue = propertyJson.get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
