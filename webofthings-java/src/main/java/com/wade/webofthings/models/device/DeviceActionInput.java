package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceActionInput {
    private String type;
    private List<String> required = new ArrayList<>();
    private List<DeviceProperty> properties = new ArrayList<>();
}
