package com.wade.webofthings.models.home;

import com.wade.webofthings.utils.constants.VocabularyConstants;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldId;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonldResource
@JsonldNamespace(name = "vcard", uri = VocabularyConstants.VCARD_URL)

public class Home {
    @JsonldId
    private String uri;
    @JsonldProperty("vcard:UID")
    private String id;
    @JsonldProperty("vcard:NICKNAME")
    private String name;
    @JsonldProperty(VocabularyConstants.VCARD4_URL + "hasMember")
    private Set<HomeUserIdentifier> users = new HashSet<>();
    @JsonldProperty(VocabularyConstants.VCARD4_URL + "hasRelated")
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

    public String getUri() {
        this.uri = "/homes/" + id;
        return uri;
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
