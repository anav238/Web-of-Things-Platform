package com.wade.webofthings.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.device.Device;
import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.dataset.parsers.DeviceResourceParser;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/devices")
    ResponseEntity<Device> newDevice(@RequestBody Device newDevice) {
        dataset.begin(ReadWrite.WRITE) ;

        newDevice.setId(String.valueOf(UUID.randomUUID()));
        String deviceURI = "/devices/" + newDevice.getId();

        // {
        //        "id": "4b912013-1caf-480b-b48d-4420c4d229e4",
        //        "name": null,
        //        "description": "test device",
        //        "category": null,
        //        "baseLink": null,
        //        "properties": null,
        //        "actions": null,
        //        "events": null,
        //        "links": null
        //    }
        Resource deviceResource = model.createResource(deviceURI)
                .addProperty(VCARD.UID, newDevice.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.DEVICE))
                .addProperty(new PropertyImpl(WOT.TITLE), newDevice.getTitle())
                .addProperty(new PropertyImpl(WOT.DESCRIPTION), newDevice.getDescription());

        for (DeviceProperty property:newDevice.getProperties()) {
            deviceResource.addProperty(new PropertyImpl(WOT.HAS_PROPERTY_AFFORDANCE), DevicePropertyMapper.mapToResource(model, property));
        }

        System.out.println(deviceResource.getProperty(new PropertyImpl(WOT.HAS_PROPERTY_AFFORDANCE)));

        dataset.commit();

        return ResponseEntity.ok(newDevice);
    }

}
