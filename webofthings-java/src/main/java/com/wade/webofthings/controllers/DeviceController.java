package com.wade.webofthings.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.models.device.Device;
import com.wade.webofthings.models.device.DeviceAction;
import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.dataset.parsers.DeviceResourceParser;
import com.wade.webofthings.utils.mappers.DeviceActionMapper;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.ok(DeviceResourceParser.getDeviceById(dataset, model, id));
    }

    @PostMapping("/devices")
    ResponseEntity<Device> newDevice(@RequestBody Device newDevice) {
        dataset.begin(ReadWrite.WRITE);

        newDevice.setId(String.valueOf(UUID.randomUUID()));
        String deviceURI = "/devices/" + newDevice.getId();

        Resource deviceResource = model.createResource(deviceURI)
                .addProperty(VCARD.UID, newDevice.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.DEVICE))
                .addProperty(new PropertyImpl(WOT.TITLE), newDevice.getTitle())
                .addProperty(new PropertyImpl(WOT.DESCRIPTION), newDevice.getDescription());

        for (DeviceProperty property : newDevice.getProperties())
            deviceResource.addProperty(new PropertyImpl(WOT.HAS_PROPERTY_AFFORDANCE), DevicePropertyMapper.mapToResource(model, property));

        for (DeviceAction action : newDevice.getActions())
            deviceResource.addProperty(new PropertyImpl(WOT.HAS_ACTION_AFFORDANCE), DeviceActionMapper.mapToResource(model, action));

        dataset.commit();

        return ResponseEntity.ok(newDevice);
    }

}
