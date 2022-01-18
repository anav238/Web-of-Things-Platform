package com.wade.webofthings.models.home;

import lombok.Data;

import java.util.List;

@Data
public class Home {
    private String id;
    private String name;
    private List<HomeUserIdentifier> users;
    private List<String> deviceIds;

    public Home() {
    }

    public Home(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
