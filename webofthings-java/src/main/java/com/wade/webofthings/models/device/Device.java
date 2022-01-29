package com.wade.webofthings.models.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Device {
    private String uri;
    private String id;
    private String title;
    private String description;
    private DeviceCategory category;
    private String baseLink;
    private Set<DeviceProperty> properties = new HashSet<>();
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
