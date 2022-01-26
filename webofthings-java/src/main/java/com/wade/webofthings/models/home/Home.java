package com.wade.webofthings.models.home;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Home {
    private String id;
    private String name;
    private Set<HomeUserIdentifier> users = new HashSet<>();
    private Set<String> deviceIds = new HashSet<>();

    public Home() {
    }

    public Home(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Home(String id, String name, Set<HomeUserIdentifier> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public Home(String id, String name, List<HomeUserIdentifier> users, Set<String> deviceIds) {
        this.id = id;
        this.name = name;
        this.deviceIds = deviceIds;
    }

    public void addUserWithRole(HomeUserIdentifier homeUserIdentifier) {
        if (users == null)
            users = new HashSet<>();

        users.add(homeUserIdentifier);
    }

    public void addDeviceId(String deviceId) {
        if (deviceIds == null)
            deviceIds = new HashSet<String>();

        deviceIds.add(deviceId);
    }
}
