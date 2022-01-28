package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceAction {
    private String name;
    private String title;
    private String description;
    private DeviceActionInput input;
    //private List<String> links = new ArrayList<>();
}
