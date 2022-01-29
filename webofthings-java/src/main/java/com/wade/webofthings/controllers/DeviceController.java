package com.wade.webofthings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.models.device.*;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.constants.WOT;
import com.wade.webofthings.utils.dataset.parsers.DeviceResourceParser;
import com.wade.webofthings.utils.http.HTTPClient;
import com.wade.webofthings.utils.mappers.DeviceActionMapper;
import com.wade.webofthings.utils.mappers.DeviceMapper;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
public class DeviceController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/devices")
    ResponseEntity<List<Device>> all() {
        return ResponseEntity.ok(DeviceResourceParser.getAllDevices(dataset, model));
    }

    @GetMapping("/devices/{id}")
    ResponseEntity<Device> one(@PathVariable String id) {
        try {
            return ResponseEntity.ok(DeviceResourceParser.getDeviceById(dataset, model, id));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @GetMapping("/devices/{id}/properties")
    ResponseEntity<Set<DeviceProperty>> getDeviceProperties(@PathVariable String id) {
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        return ResponseEntity.ok(device.getProperties());
    }

    @GetMapping("/devices/{id}/properties/{propertyName}")
    ResponseEntity<DeviceProperty> getDeviceProperty(@PathVariable String id, @PathVariable String propertyName) {
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        for (DeviceProperty deviceProperty : device.getProperties())
            if (deviceProperty.getName().equals(propertyName))
                return ResponseEntity.ok(deviceProperty);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property Not Found");
    }

    @GetMapping("/devices/{id}/actions")
    ResponseEntity<Set<DeviceAction>> getDeviceActions(@PathVariable String id) {
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        return ResponseEntity.ok(device.getActions());
    }

    @PostMapping("/devices/{id}/actions")
    public ResponseEntity<String> executeDeviceAction(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String requestUrl = device.getBaseLink() + "/actions";
        try {
            return HTTPClient.sendPostRequest(requestUrl, payload);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
        }
    }

    @GetMapping("/devices/{id}/actions/{actionName}")
    ResponseEntity<DeviceAction> getDeviceAction(@PathVariable String id, @PathVariable String actionName) {
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        for (DeviceAction deviceAction : device.getActions())
            if (deviceAction.getName().equals(actionName))
                return ResponseEntity.ok(deviceAction);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Action not Found");
    }

    @PostMapping("/devices")
    ResponseEntity<Device> newDeviceByUrl(@RequestBody CreateDeviceByUrl requestBody) {
        String specification = HTTPClient.sendGetRequest(requestBody.getDeviceUrl());
        try {
            Map<String, Object> specificationJson = objectMapper.readValue(specification, new TypeReference<Map<String, Object>>() {
            });
            Device device = DeviceMapper.mapDeviceSpecificationToDevice(specificationJson);
            device.setCategory(DeviceCategory.valueOf(requestBody.getDeviceCategory()));
            device.setBaseLink(requestBody.getDeviceUrl());
            return newDevice(device);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new Device());
    }

    ResponseEntity<Device> newDevice(@RequestBody Device newDevice) {
        dataset.begin(ReadWrite.WRITE);

        newDevice.setId(String.valueOf(UUID.randomUUID()));
        String deviceURI = "/devices/" + newDevice.getId();

        Resource deviceResource = model.createResource(deviceURI)
                .addProperty(VCARD.UID, newDevice.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.DEVICE))
                .addProperty(new PropertyImpl(WOT.TITLE), newDevice.getTitle())
                .addProperty(new PropertyImpl(WOT.DESCRIPTION), newDevice.getDescription())
                .addProperty(new PropertyImpl(WOT.HAS_LINK), newDevice.getBaseLink())
                .addProperty(VCARD.CATEGORIES, String.valueOf(newDevice.getCategory()));

        for (DeviceProperty property : newDevice.getProperties())
            deviceResource.addProperty(new PropertyImpl(WOT.HAS_PROPERTY_AFFORDANCE), DevicePropertyMapper.mapToResource(model, property));

        for (DeviceAction action : newDevice.getActions())
            deviceResource.addProperty(new PropertyImpl(WOT.HAS_ACTION_AFFORDANCE), DeviceActionMapper.mapToResource(model, action));

        dataset.commit();

        return ResponseEntity.ok(newDevice);
    }

    @DeleteMapping("/devices/{id}")
    public void deleteDevice(@PathVariable String id) {
        //remove all statements mentioning the device
        Resource home = model.getResource("/devices/" + id);
        DatasetUtils.deleteResource(dataset, model, home);
    }

}
