package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.utils.constants.VocabularyConstants;
import com.wade.webofthings.utils.http.HTTPClient;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonldResource
@JsonldNamespace(name = "schema", uri = VocabularyConstants.SCHEMA_URL)
public class DeviceProperty {
    @JsonldProperty("schema:type")
    private String type;
    @JsonldProperty(VocabularyConstants.WOT_URL + "name")
    private String name;
    @JsonldProperty(VocabularyConstants.WOT_URL + "title")
    private String title;
    @JsonldProperty("schema:value")
    private String currentValue;
    @JsonldProperty("schema:minValue")
    private String minimum;
    @JsonldProperty("schema:maxValue")
    private String maximum;
    @JsonldProperty("schema:unitText")
    private String unit;
    @JsonldProperty(VocabularyConstants.WOT_URL + "description")
    private String description;

    public DeviceProperty() {
    }

    public void updateCurrentValue(String baseLink, ObjectMapper objectMapper) {
        if (baseLink == null)
            return;
        try {
            String response = HTTPClient.sendGetRequest(baseLink + "/properties/" + name);
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
