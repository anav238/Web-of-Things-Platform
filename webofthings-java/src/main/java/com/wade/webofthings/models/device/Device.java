package com.wade.webofthings.models.device;

import com.wade.webofthings.models.enums.DeviceCategory;
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
    private List<DeviceProperty> properties;
    private List<DeviceAction> actions;
    private List<String> events;
    private List<String> links;

    public Device(String id, String title, String description, List<DeviceProperty> properties) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.properties = properties;
    }

    public void addProperty(DeviceProperty deviceProperty) {
        if (properties != null)
            properties.add(deviceProperty);
        else {
            properties = new ArrayList<>();
            properties.add(deviceProperty);
        }
    }
}
