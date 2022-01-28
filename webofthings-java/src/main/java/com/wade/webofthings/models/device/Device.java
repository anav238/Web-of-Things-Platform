package com.wade.webofthings.models.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Device {
    private String uri;
    private String id;
    private String title;
    private String description;
    private DeviceCategory category;
    private String baseLink;
    private List<DeviceProperty> properties = new ArrayList<>();
    private List<DeviceAction> actions = new ArrayList<>();
    private List<String> events = new ArrayList<>();

    public Device() {}

    public Device(String id, String title, String description, List<DeviceProperty> properties, List<DeviceAction> actions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.properties = properties;
        this.actions = actions;
    }

    public String getUri() {
        this.uri = "/devices/" + id;
        return uri;
    }

    public void addProperty(DeviceProperty deviceProperty) {
        if (properties == null)
            properties = new ArrayList<>();
        properties.add(deviceProperty);
    }

    public void addAction(DeviceAction deviceAction) {
        if (actions == null)
            actions = new ArrayList<>();
        actions.add(deviceAction);
    }

    public void updatePropertiesWithCurrentValues() {
        if (baseLink == null)
            return;
        ObjectMapper objectMapper = new ObjectMapper();
        for (DeviceProperty deviceProperty:getProperties())
            deviceProperty.updateCurrentValue(baseLink, objectMapper);
    }
}
