package com.wade.webofthings.utils.mappers;

import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.utils.Constants.Schema;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.DatasetUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;

import java.util.HashMap;
import java.util.Map;

public class DevicePropertyMapper {
    public static Resource mapToResource(Model model, DeviceProperty deviceProperty) {
        Resource devicePropertyResource = model.createResource().addProperty(VCARD.CLASS, "deviceProperty");

        Map<Property, String> propertyStringMap = new HashMap<Property, String>() {{
            put(new PropertyImpl(WOT.NAME), deviceProperty.getTitle());
            put(new PropertyImpl(WOT.DESCRIPTION), deviceProperty.getDescription());
            put(new PropertyImpl(Schema.UNIT_TEXT), deviceProperty.getUnit());
            put(new PropertyImpl(Schema.TYPE), deviceProperty.getType());
            put(new PropertyImpl(Schema.VALUE_TYPE), deviceProperty.getValueType());
            put(new PropertyImpl(Schema.VALUE), deviceProperty.getCurrentValue());

            if (deviceProperty.getValueType() != null && deviceProperty.getValueType().equals("number")) {
                put(new PropertyImpl(Schema.MINIMUM), String.valueOf(deviceProperty.getMinimum()));
                put(new PropertyImpl(Schema.MAXIMUM), String.valueOf(deviceProperty.getMaximum()));
            }

        }};

        for (var propertyAndValue:propertyStringMap.entrySet()) {
            if (propertyAndValue.getValue() != null)
                devicePropertyResource.addProperty(propertyAndValue.getKey(), propertyAndValue.getValue());
        }

        for (String link:deviceProperty.getLinks())
            devicePropertyResource.addProperty(new PropertyImpl(WOT.HAS_LINK), link);

        return devicePropertyResource;
    }

    public static DeviceProperty mapResourceToDeviceProperty(Resource resource) {
        DeviceProperty deviceProperty = new DeviceProperty();
        deviceProperty.setTitle(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.NAME))));
        deviceProperty.setDescription(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(WOT.DESCRIPTION))));
        deviceProperty.setUnit(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.UNIT_TEXT))));
        deviceProperty.setType(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.TYPE))));
        deviceProperty.setValueType(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.VALUE_TYPE))));
        deviceProperty.setCurrentValue(DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.VALUE))));

        String minimumString = DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.MINIMUM)));
        if (minimumString != null)
            deviceProperty.setMinimum(Double.parseDouble(minimumString));

        String maximumString = DatasetUtils.getStatementStringOrNull(resource.getProperty(new PropertyImpl(Schema.MAXIMUM)));
        if (maximumString != null)
            deviceProperty.setMaximum(Double.parseDouble(maximumString));

        return deviceProperty;
    }
}
