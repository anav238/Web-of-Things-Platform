package com.wade.webofthings.utils.mappers;

import com.wade.webofthings.models.device.DeviceAction;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.constants.WOT;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;

import java.util.HashMap;
import java.util.Map;

public class DeviceActionMapper {
    public static Resource mapToResource(Model model, DeviceAction deviceAction) {
        Resource deviceActionResource = model.createResource().addProperty(VCARD.CLASS, "deviceAction");

        Map<Property, String> propertyStringMap = new HashMap<>() {{
            put(new PropertyImpl(WOT.NAME), deviceAction.getName());
            put(new PropertyImpl(WOT.TITLE), deviceAction.getTitle());
            put(new PropertyImpl(WOT.DESCRIPTION), deviceAction.getDescription());
        }};

        for (var propertyAndValue : propertyStringMap.entrySet()) {
            if (propertyAndValue.getValue() != null)
                deviceActionResource.addProperty(propertyAndValue.getKey(), propertyAndValue.getValue());
        }

        deviceActionResource.addProperty(new PropertyImpl(WOT.HAS_INPUT_SCHEMA), DeviceActionInputMapper.mapToResource(model, deviceAction.getInput()));

        return deviceActionResource;
    }

    public static DeviceAction mapResourceToDeviceAction(Resource resource) {
        DeviceAction deviceAction = new DeviceAction();
        deviceAction.setTitle(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.TITLE))));
        deviceAction.setName(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.NAME))));
        deviceAction.setDescription(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.DESCRIPTION))));

        Resource deviceActionInputResource = resource.getProperty(new PropertyImpl(WOT.HAS_INPUT_SCHEMA)).getResource();
        deviceAction.setInput(DeviceActionInputMapper.mapResourceToDeviceActionInput(deviceActionInputResource));

        return deviceAction;
    }

}
