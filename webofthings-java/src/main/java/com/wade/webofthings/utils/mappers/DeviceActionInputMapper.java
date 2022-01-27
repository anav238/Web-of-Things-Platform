package com.wade.webofthings.utils.mappers;

import com.wade.webofthings.models.device.DeviceActionInput;
import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.utils.Constants.JsonSchema;
import com.wade.webofthings.utils.Constants.Schema;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.DatasetUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceActionInputMapper {
    public static Resource mapToResource(Model model, DeviceActionInput deviceActionInput) {
        Resource deviceActionInputResource = model.createResource().addProperty(VCARD.CLASS, "deviceActionInput");

        Map<Property, String> propertyStringMap = new HashMap<>() {{
            put(new PropertyImpl(Schema.TYPE), deviceActionInput.getType());
        }};

        for (var propertyAndValue:propertyStringMap.entrySet()) {
            if (propertyAndValue.getValue() != null)
                deviceActionInputResource.addProperty(propertyAndValue.getKey(), propertyAndValue.getValue());
        }

        if (deviceActionInput.getRequired() != null)
        for (String required:deviceActionInput.getRequired())
            deviceActionInputResource.addProperty(new PropertyImpl(JsonSchema.REQUIRES), required);

        if (deviceActionInput.getProperties() != null)
        for (DeviceProperty deviceProperty:deviceActionInput.getProperties())
            deviceActionInputResource.addProperty(new PropertyImpl(WOT.HAS_PROPERTY_AFFORDANCE),DevicePropertyMapper.mapToResource(model, deviceProperty));

        return deviceActionInputResource;
    }

    public static DeviceActionInput mapResourceToDeviceActionInput(Resource resource) {
        DeviceActionInput deviceActionInput = new DeviceActionInput();
        deviceActionInput.setType(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.TYPE))));

        List<String> required = new ArrayList<>();
        StmtIterator it = resource.listProperties(new PropertyImpl(JsonSchema.REQUIRES));
        while (it.hasNext()) {
            Statement stmt = it.nextStatement();
            required.add(stmt.getObject().toString());
        }
        deviceActionInput.setRequired(required);

        List<DeviceProperty> properties = new ArrayList<>();
        it = resource.listProperties(new PropertyImpl(WOT.HAS_PROPERTY_AFFORDANCE));
        while (it.hasNext()) {
            Statement stmt = it.nextStatement();
            Resource devicePropertyResource = stmt.getObject().asResource();
            properties.add(DevicePropertyMapper.mapResourceToDeviceProperty(devicePropertyResource));
        }
        deviceActionInput.setProperties(properties);

        return deviceActionInput;
    }

}
