package com.wade.webofthings.models;

import com.wade.webofthings.models.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class Home {
    private String id;
    private String name;
    private List<UserHomeIdentifier> users;
    private List<String> deviceIds;
}
