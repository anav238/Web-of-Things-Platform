package com.wade.webofthings.models.device;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Device {
    private String id;
    private String title;
    private String description;
    private DeviceCategory category;
    private String baseLink;
    private List<DeviceProperty> properties = new ArrayList<>();
    private List<DeviceAction> actions = new ArrayList<>();
    private List<String> events = new ArrayList<>();
    private List<String> links = new ArrayList<>();

    public Device() {}

    public Device(String id, String title, String description, List<DeviceProperty> properties, List<DeviceAction> actions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.properties = properties;
        this.actions = actions;
    }

    public void addProperty(DeviceProperty deviceProperty) {
        if (properties != null)
            properties.add(deviceProperty);
        else {
            properties = new ArrayList<>();
            properties.add(deviceProperty);
        }
    }

    public void addAction(DeviceAction deviceAction) {
        if (actions != null)
            actions.add(deviceAction);
        else {
            actions = new ArrayList<>();
            actions.add(deviceAction);
        }
    }
}
