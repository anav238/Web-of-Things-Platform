package com.wade.webofthings.models.home;

import com.wade.webofthings.models.user.UserRole;
import lombok.Data;
import org.apache.jena.rdf.model.Statement;

@Data
public class HomeUserIdentifier {
    private String userId;
    private UserRole userRole;

    public HomeUserIdentifier() {}

    public HomeUserIdentifier(String userId, UserRole userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }
}
