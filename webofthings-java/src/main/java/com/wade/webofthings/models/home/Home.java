package com.wade.webofthings.models.home;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Home {
    private String id;
    private String name;
    private List<HomeUserIdentifier> users = new ArrayList<>();
    private List<String> deviceIds = new ArrayList<>();

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

    public Home(String id, String name, List<HomeUserIdentifier> users, List<String> deviceIds) {
        this.id = id;
        this.name = name;
        this.deviceIds = deviceIds;
    }

    public void addUserWithRole(HomeUserIdentifier homeUserIdentifier) {
        if (users == null)
            users = new ArrayList<>();

        users.add(homeUserIdentifier);
    }

    public void addDeviceId(String deviceId) {
        if (deviceIds == null)
            deviceIds = new ArrayList<>();

        deviceIds.add(deviceId);
    }
}
