package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.utils.http.HTTPClient;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

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
            Map<String, String> propertyJson = objectMapper.readValue(response, new TypeReference<>() {
            });
            currentValue = propertyJson.get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceProperty that = (DeviceProperty) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
