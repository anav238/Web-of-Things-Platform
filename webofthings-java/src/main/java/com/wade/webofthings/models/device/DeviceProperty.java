package com.wade.webofthings.models.device;

import lombok.Data;

import java.util.List;

@Data
public class DeviceProperty {
    private String id;
    private String type;
    private String name;
    private String valueType;
    private String currentValue;
    private double minimum;
    private double maximum;
    private String unit;
    private String description;
    private List<String> links;

    public DeviceProperty() {
    }

    public DeviceProperty(String id) {
        this.id = id;
    }
}
