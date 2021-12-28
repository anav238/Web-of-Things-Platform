package com.wade.webofthings.models.device;

import com.wade.webofthings.models.enums.DeviceCategory;
import lombok.Data;

import java.util.List;

@Data
public class Device {
    private String id;
    private String name;
    private String description;
    private DeviceCategory category;
    private String baseLink;
    private List<DeviceProperty> properties;
    private List<DeviceAction> actions;
    private List<String> events;
    private List<String> links;
}
