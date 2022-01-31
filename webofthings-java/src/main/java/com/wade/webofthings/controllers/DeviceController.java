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
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
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
@CrossOrigin
public class DeviceController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JsonldModule());

    @RequestMapping(value = "/devices", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> all() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(DeviceResourceParser.getAllDevices(dataset, model)));
    }

    @RequestMapping(value = "/devices/{id}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> one(@PathVariable String id) throws JsonProcessingException {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(DeviceResourceParser.getDeviceById(dataset, model, id)));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/properties", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getDeviceProperties(@PathVariable String id) throws JsonProcessingException {
        try {
            Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
            return ResponseEntity.ok(objectMapper.writeValueAsString(device.getProperties()));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/properties/{propertyName}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getDeviceProperty(@PathVariable String id, @PathVariable String propertyName) throws JsonProcessingException {
        try {
            Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
            for (DeviceProperty deviceProperty : device.getProperties())
                if (deviceProperty.getName().equals(propertyName))
                    return ResponseEntity.ok(objectMapper.writeValueAsString(deviceProperty));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Property Not Found");
    }

    @RequestMapping(value = "/devices/{id}/actions", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getDeviceActions(@PathVariable String id) throws JsonProcessingException {
        try {
            Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
            return ResponseEntity.ok(objectMapper.writeValueAsString(device.getActions()));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/actions", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> executeDeviceAction(@PathVariable String id, @RequestBody Map<String, Object> payload) {
        try {
            Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
            String requestUrl = device.getBaseLink() + "/actions";
            try {
                return HTTPClient.sendPostRequest(requestUrl, payload);
            } catch (HttpClientErrorException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
            }
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/actions/{actionName}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getDeviceAction(@PathVariable String id, @PathVariable String actionName) throws JsonProcessingException {
        try {
            Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
            for (DeviceAction deviceAction : device.getActions())
                if (deviceAction.getName().equals(actionName))
                    return ResponseEntity.ok(objectMapper.writeValueAsString(deviceAction));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Action not Found");
    }

    @RequestMapping(value = "/devices", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> newDeviceByUrl(@RequestBody CreateDeviceByUrl requestBody) {
        String specification = HTTPClient.sendGetRequest(requestBody.getDeviceUrl());
        try {
            Map<String, Object> specificationJson = objectMapper.readValue(specification, new TypeReference<Map<String, Object>>() {
            });
            Device device = DeviceMapper.mapDeviceSpecificationToDevice(specificationJson);
            device.setCategory(DeviceCategory.valueOf(requestBody.getDeviceCategory()));
            device.setBaseLink(requestBody.getDeviceUrl());
            return ResponseEntity.ok(objectMapper.writeValueAsString(newDevice(device)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error creating device");
        }
    }

    Device newDevice(@RequestBody Device newDevice) {
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

        return newDevice;
    }

    @DeleteMapping("/devices/{id}")
    public void deleteDevice(@PathVariable String id) {
        //remove all statements mentioning the device
        Resource home = model.getResource("/devices/" + id);
        DatasetUtils.deleteResource(dataset, model, home);
    }

}
