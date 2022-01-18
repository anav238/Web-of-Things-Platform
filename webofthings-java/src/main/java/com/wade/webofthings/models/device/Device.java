package com.wade.webofthings.models.device;

import lombok.Data;

import java.util.List;

@Data
public class Device {
    private String id;
    private String title;
    private String description;
    private DeviceCategory category;
    private String baseLink;
    private List<DeviceProperty> properties;
    private List<DeviceAction> actions;
    private List<String> events;
    private List<String> links;

    public Device() { }

    public Device(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
