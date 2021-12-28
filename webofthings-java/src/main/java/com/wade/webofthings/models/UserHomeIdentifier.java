package com.wade.webofthings.models;

import com.wade.webofthings.models.enums.UserRole;
import lombok.Data;

@Data
public class UserHomeIdentifier {
    private String userId;
    private UserRole userRole;
}
