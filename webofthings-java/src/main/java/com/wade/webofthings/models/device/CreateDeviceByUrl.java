package com.wade.webofthings.models.device;

import lombok.Data;

@Data
public class CreateDeviceByUrl {
    private String deviceUrl;
    private String deviceCategory;
}
