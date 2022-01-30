package com.wade.webofthings.models.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.utils.constants.VocabularyConstants;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldId;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@JsonldResource
@JsonldNamespace(name = "wot", uri = VocabularyConstants.WOT_URL)
public class Device {
    @JsonldId
    private String uri;
    @JsonldProperty(VocabularyConstants.VCARD_URL + "UID")
    private String id;
    @JsonldProperty("wot:title")
    private String title;
    @JsonldProperty("wot:description")
    private String description;
    @JsonldProperty(VocabularyConstants.VCARD_URL + "categories")
    private DeviceCategory category;
    @JsonldProperty("wot:hasLink")
    private String baseLink;
    @JsonldProperty("wot:hasPropertyAffordance")
    private Set<DeviceProperty> properties = new HashSet<>();
    @JsonldProperty("wot:hasActionAffordance")
    private Set<DeviceAction> actions = new HashSet<>();

    public Device() {
    }

    public String getUri() {
        this.uri = "/devices/" + id;
        return uri;
    }

    public void addProperty(DeviceProperty deviceProperty) {
        if (properties == null)
            properties = new HashSet<>();
        properties.add(deviceProperty);
    }

    public void addAction(DeviceAction deviceAction) {
        if (actions == null)
            actions = new HashSet<>();
        actions.add(deviceAction);
    }

    public void updatePropertiesWithCurrentValues() {
        if (baseLink == null)
            return;
        ObjectMapper objectMapper = new ObjectMapper();
        for (DeviceProperty deviceProperty : getProperties())
            deviceProperty.updateCurrentValue(baseLink, objectMapper);
    }
}
