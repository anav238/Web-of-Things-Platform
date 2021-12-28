package com.wade.webofthings.models.device;

import lombok.Data;

import java.util.List;

@Data
public class DeviceActionInput {
    private String type;
    private List<String> required;
    private List<DeviceProperty> properties;
}
