package com.wade.webofthings.utils.mappers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.models.device.Device;
import com.wade.webofthings.models.device.DeviceAction;
import com.wade.webofthings.models.device.DeviceActionInput;
import com.wade.webofthings.models.device.DeviceProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeviceMapper {

    final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

    public static Device mapDeviceSpecificationToDevice(Map<String, Object> deviceSpecification) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Device device = new Device();
        device.setTitle(deviceSpecification.getOrDefault("title", "").toString());
        device.setDescription(deviceSpecification.getOrDefault("description", "").toString());

        Map<String, Object> properties = (Map<String, Object>) deviceSpecification.getOrDefault("properties", null);
        if (properties != null) {
            for (var propertyEntry:properties.entrySet()) {
                DeviceProperty deviceProperty = mapper.convertValue(propertyEntry.getValue(), DeviceProperty.class);
                device.addProperty(deviceProperty);
            }
        }

        Map<String, Object> actions = (Map<String, Object>) deviceSpecification.getOrDefault("actions", null);
        if (actions != null) {
            for (Map.Entry<String, Object> actionEntry:actions.entrySet()) {
                Map<String,Object> deviceActionMap = (Map<String, Object>) actionEntry.getValue();
                DeviceAction deviceAction = new DeviceAction();
                deviceAction.setTitle(deviceActionMap.getOrDefault("title", "").toString());
                deviceAction.setDescription(deviceActionMap.getOrDefault("description", "").toString());

                DeviceActionInput input = new DeviceActionInput();
                Map<String, Object> inputMap = (Map<String, Object>) deviceActionMap.getOrDefault("input", null);
                input.setType(inputMap.getOrDefault("type", null).toString());
                input.setRequired((List<String>) inputMap.getOrDefault("required", null));

                List<DeviceProperty> inputProperties = new ArrayList<>();
                Map<String, Object> inputPropertiesMap = (Map<String, Object>) inputMap.getOrDefault("properties", null);
                for (Map.Entry<String, Object> inputPropertiesEntry:inputPropertiesMap.entrySet()) {
                    DeviceProperty deviceActionProperty = mapper.convertValue(inputPropertiesEntry.getValue(), DeviceProperty.class);
                    inputProperties.add(deviceActionProperty);
                }
                input.setProperties(inputProperties);

                deviceAction.setInput(input);
                device.addAction(deviceAction);
            }
        }
        return device;
    }
}
