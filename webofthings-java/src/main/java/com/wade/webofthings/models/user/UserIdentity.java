package com.wade.webofthings.models.user;

import io.jsonwebtoken.Claims;

public class UserIdentity {
    private boolean Authorized;
    private Claims Claims;
    private String UserId;

    public UserIdentity(boolean authorized, io.jsonwebtoken.Claims claims, String userId) {
        Authorized = authorized;
        Claims = claims;
        UserId = userId;
    }

    public static UserIdentity Unathorized() {
        return new UserIdentity(false, null, null);
    }

    public boolean isAuthorized() {
        return Authorized;
    }

    public io.jsonwebtoken.Claims getClaims() {
        return Claims;
    }

    public String getUserId() {
        return UserId;
    }

}
