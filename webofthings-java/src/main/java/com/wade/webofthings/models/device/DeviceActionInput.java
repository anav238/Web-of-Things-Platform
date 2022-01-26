package com.wade.webofthings.models.device;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeviceActionInput {
    private String type;
    private List<String> required = new ArrayList<>();
    private List<DeviceProperty> properties = new ArrayList<>();
}
