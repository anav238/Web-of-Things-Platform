package com.wade.webofthings.models.user;

import lombok.Data;

@Data
public class PublicUser {
    private String id;
    private String username;

    public PublicUser() {
    }

    public PublicUser(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
