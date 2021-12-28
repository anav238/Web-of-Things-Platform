package com.wade.webofthings.models.device;

import lombok.Data;

import java.util.List;

@Data
public class DeviceAction {
    private String title;
    private String description;
    private List<DeviceActionInput> input;
    private List<String> links;
}
