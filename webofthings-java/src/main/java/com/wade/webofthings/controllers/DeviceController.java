package com.wade.webofthings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.models.device.*;
import com.wade.webofthings.models.user.UserIdentity;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.constants.WOT;
import com.wade.webofthings.utils.dataset.parsers.DeviceResourceParser;
import com.wade.webofthings.utils.dataset.parsers.HomeResourceParser;
import com.wade.webofthings.utils.dataset.parsers.UserResourceParser;
import com.wade.webofthings.utils.http.HTTPClient;
import com.wade.webofthings.utils.mappers.DeviceActionMapper;
import com.wade.webofthings.utils.mappers.DeviceMapper;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import org.apache.jena.base.Sys;
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

import java.util.Map;
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
    ResponseEntity<String> all(@RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        return ResponseEntity.ok(objectMapper.writeValueAsString(DeviceResourceParser.getAllDevices(dataset, model)));
    }

    @RequestMapping(value = "/devices/{id}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> one(@PathVariable String id, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String category= String.valueOf(device.getCategory());

        if(category.contentEquals("ENVIRONMENT"))
            if(role.contentEquals("GUEST"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if(category.contentEquals("SECURITY"))
            if(role.contentEquals("GUEST")||role.contentEquals("CHILD"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(device));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/properties", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getDeviceProperties(@PathVariable String id, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String category= String.valueOf(device.getCategory());

        if(category.contentEquals("ENVIRONMENT"))
            if(role.contentEquals("GUEST"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if(category.contentEquals("SECURITY"))
            if(role.contentEquals("GUEST")||role.contentEquals("CHILD"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(device.getProperties()));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/properties/{propertyName}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getDeviceProperty(@PathVariable String id, @PathVariable String propertyName, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String category= String.valueOf(device.getCategory());

        if(category.contentEquals("ENVIRONMENT"))
            if(role.contentEquals("GUEST"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if(category.contentEquals("SECURITY"))
            if(role.contentEquals("GUEST")||role.contentEquals("CHILD"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
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
    ResponseEntity<String> getDeviceActions(@PathVariable String id, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String category= String.valueOf(device.getCategory());

        if(category.contentEquals("ENVIRONMENT"))
            if(role.contentEquals("GUEST"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if(category.contentEquals("SECURITY"))
            if(role.contentEquals("GUEST")||role.contentEquals("CHILD"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(device.getActions()));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not Found");
        }
    }

    @RequestMapping(value = "/devices/{id}/actions", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> executeDeviceAction(@PathVariable String id, @RequestBody Map<String, Object> payload, @RequestHeader(value = "authorization") String jwt) {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String category= String.valueOf(device.getCategory());

        if(category.contentEquals("ENVIRONMENT"))
            if(role.contentEquals("GUEST"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if(category.contentEquals("SECURITY"))
            if(role.contentEquals("GUEST")||role.contentEquals("CHILD"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            String requestUrl = device.getBaseLink() + "/actions";
            try {
                return ResponseEntity.ok(HTTPClient.sendPostRequest(requestUrl, payload).getBody());
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
    ResponseEntity<String> getDeviceAction(@PathVariable String id, @PathVariable String actionName, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        Device device = DeviceResourceParser.getDeviceById(dataset, model, id);
        String category= String.valueOf(device.getCategory());

        if(category.contentEquals("ENVIRONMENT"))
            if(role.contentEquals("GUEST"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if(category.contentEquals("SECURITY"))
            if(role.contentEquals("GUEST")||role.contentEquals("CHILD"))
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
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
    ResponseEntity<String> newDeviceByUrl(@RequestBody CreateDeviceByUrl requestBody,  @RequestHeader(value = "authorization") String jwt) {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

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
    public void deleteDevice(@PathVariable String id, @RequestHeader(value = "authorization") String jwt) {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForDeviceId(dataset, model, id, identity.getUserId());
        if(role.contentEquals("GUEST")||role.contentEquals("CHILD")||role.contentEquals("MEMBER"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        //remove all statements mentioning the device
        Resource home = model.getResource("/devices/" + id);
        DatasetUtils.deleteResource(dataset, model, home);
    }

}
