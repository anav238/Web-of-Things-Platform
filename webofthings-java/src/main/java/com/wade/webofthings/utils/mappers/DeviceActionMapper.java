package com.wade.webofthings.utils.mappers;

import com.wade.webofthings.models.device.DeviceAction;
import com.wade.webofthings.utils.Constants.JsonSchema;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.DatasetUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;

import java.util.HashMap;
import java.util.Map;

public class DeviceActionMapper {
    public static Resource mapToResource(Model model, DeviceAction deviceAction) {
        Resource deviceActionResource = model.createResource().addProperty(VCARD.CLASS, "deviceAction");

        Map<Property, String> propertyStringMap = new HashMap<>() {{
            put(new PropertyImpl(WOT.NAME), deviceAction.getTitle());
            put(new PropertyImpl(WOT.DESCRIPTION), deviceAction.getDescription());
        }};

        for (var propertyAndValue:propertyStringMap.entrySet()) {
            if (propertyAndValue.getValue() != null)
                deviceActionResource.addProperty(propertyAndValue.getKey(), propertyAndValue.getValue());
        }

        deviceActionResource.addProperty(new PropertyImpl(WOT.HAS_INPUT_SCHEMA), DeviceActionInputMapper.mapToResource(model, deviceAction.getInput()));

        for (String link:deviceAction.getLinks())
            deviceActionResource.addProperty(new PropertyImpl(WOT.HAS_LINK), link);

        return deviceActionResource;
    }

    public static DeviceAction mapResourceToDeviceAction(Resource resource) {
        DeviceAction deviceAction = new DeviceAction();
        deviceAction.setTitle(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.NAME))));
        deviceAction.setDescription(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.DESCRIPTION))));

        StmtIterator it = resource.listProperties(new PropertyImpl(JsonSchema.REQUIRES));

        Resource deviceActionInputResource = resource.getProperty(new PropertyImpl(WOT.HAS_INPUT_SCHEMA)).getResource();
        deviceAction.setInput(DeviceActionInputMapper.mapResourceToDeviceActionInput(deviceActionInputResource));

        return deviceAction;
    }

}
