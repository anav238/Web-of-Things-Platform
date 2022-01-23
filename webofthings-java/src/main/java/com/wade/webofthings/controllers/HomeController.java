package com.wade.webofthings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.home.Home;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.models.home.HomeUserIdentifier;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.dataset.parsers.HomeResourceParser;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import com.wade.webofthings.utils.mappers.HomeUserIdentifierMapper;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.VCARD4;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class HomeController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/homes")
    ResponseEntity<List<Home>> all() {
        return ResponseEntity.ok(HomeResourceParser.getAllHomes(dataset, model));
    }

    @GetMapping("/homes/{id}")
    ResponseEntity<Home> one(@PathVariable String id) {
        return ResponseEntity.ok(HomeResourceParser.getHomeById(dataset, model, id));
    }

    @PostMapping("/homes")
    ResponseEntity<Home> newHome(@RequestBody Home newHome) {
        String randomId = String.valueOf(UUID.randomUUID());
        return newHomeWithId(newHome, randomId);
    }

    private ResponseEntity<Home> newHomeWithId(Home newHome, String id) {
        dataset.begin(ReadWrite.WRITE) ;

        newHome.setId(id);
        String homeURI = "/homes/" + newHome.getId();

        Resource homeResource = model.createResource(homeURI)
                .addProperty(VCARD.UID, newHome.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.HOME))
                .addProperty(VCARD.NICKNAME, newHome.getName());

        for (HomeUserIdentifier homeUserIdentifier:newHome.getUsers())
            homeResource.addProperty(VCARD4.hasMember, HomeUserIdentifierMapper.mapToResource(model, homeUserIdentifier));

        dataset.commit();

        return ResponseEntity.ok(newHome);
    }

    @PatchMapping(path = "/homes/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Home> patchHome(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            Home home = HomeResourceParser.getHomeById(dataset, model, id);
            Home homePatched = applyPatchToHome(patch, home);
            System.out.println("home before patch: " + home.toString());
            System.out.println("patched home: " + homePatched.toString());

            deleteHome(id);
            newHomeWithId(homePatched, id);

            return ResponseEntity.ok(homePatched);
        }
        catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Home applyPatchToHome(JsonPatch patch, Home targetHome) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetHome, JsonNode.class));
        return objectMapper.treeToValue(patched, Home.class);
    }

    @DeleteMapping("/homes/{id}")
    public void deleteHome(@PathVariable String id) {
        //remove all statements mentioning the user
        Resource home = model.getResource("/homes/" + id);
        DatasetUtils.deleteResource(dataset, model, home);
    }
}
