package com.wade.webofthings.models.user;

import com.wade.webofthings.utils.constants.VocabularyConstants;
import ioinformarics.oss.jackson.module.jsonld.annotation.*;
import lombok.Data;

@Data
@JsonldResource
@JsonldNamespace(name = "vcard", uri = VocabularyConstants.VCARD_URL)
public class PublicUser {
    @JsonldId
    private String uri;
    @JsonldProperty("vcard:UID")
    private String id;
    @JsonldProperty("vcard:NICKNAME")
    private String username;

    public PublicUser(String id, String username) {
        this.id = id;
        this.username = username;
        this.uri = "/users/" + id;
    }
}
