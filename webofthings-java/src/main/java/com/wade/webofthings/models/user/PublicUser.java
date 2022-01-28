package com.wade.webofthings.models.user;

import lombok.Data;

@Data
public class PublicUser {
    private String uri;
    private String id;
    private String username;

    public PublicUser(String id, String username) {
        this.id = id;
        this.username = username;
        this.uri = "/users/" + id;
    }
}
