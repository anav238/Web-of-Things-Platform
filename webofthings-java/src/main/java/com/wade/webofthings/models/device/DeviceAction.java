package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wade.webofthings.utils.constants.VocabularyConstants;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import lombok.Data;

import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonldResource
@JsonldNamespace(name = "wot", uri = VocabularyConstants.WOT_URL)
public class DeviceAction {
    @JsonldProperty("wot:name")
    private String name;
    @JsonldProperty("wot:title")
    private String title;
    @JsonldProperty("wot:description")
    private String description;
    @JsonldProperty("wot:hasInputSchema")
    private DeviceActionInput input;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceAction that = (DeviceAction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
