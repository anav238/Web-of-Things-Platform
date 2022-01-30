package com.wade.webofthings.models.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wade.webofthings.utils.constants.VocabularyConstants;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonldResource
@JsonldNamespace(name = "schema", uri = VocabularyConstants.SCHEMA_URL)
public class DeviceActionInput {
    @JsonldProperty("schema:type")
    private String type;
    @JsonldProperty(VocabularyConstants.JSON_SCHEMA_URL + "requires")
    private List<String> required = new ArrayList<>();
    @JsonldProperty(VocabularyConstants.WOT_URL + "hasPropertyAffordance")
    private List<DeviceProperty> properties = new ArrayList<>();
}
