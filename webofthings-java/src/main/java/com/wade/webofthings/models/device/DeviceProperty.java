package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceProperty {
    private String type;
    private String title;
    private String valueType;
    private String currentValue;
    private double minimum;
    private double maximum;
    private String unit;
    private String description;
    //private List<String> links = new ArrayList<>();

    public DeviceProperty() {
    }

}
