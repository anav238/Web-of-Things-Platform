package com.wade.webofthings.models.home;

import lombok.Data;

import java.util.ArrayList;
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

    public Home(String id, String name, List<HomeUserIdentifier> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public void addUserWithRole(HomeUserIdentifier homeUserIdentifier) {
        if (users == null)
            users = new ArrayList<>();

        users.add(homeUserIdentifier);
    }
}
