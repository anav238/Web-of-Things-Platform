package com.wade.webofthings.models.user;

import com.wade.webofthings.utils.constants.VocabularyConstants;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldId;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import lombok.Data;

@Data
@JsonldResource
@JsonldNamespace(name = "vcard", uri = VocabularyConstants.VCARD_URL)
public class User {
    @JsonldId
    private String uri;
    @JsonldProperty("vcard:UID")
    private String id;
    @JsonldProperty("vcard:NICKNAME")
    private String username;
    @JsonldProperty("vcard:KEY")
    private String password;


}
