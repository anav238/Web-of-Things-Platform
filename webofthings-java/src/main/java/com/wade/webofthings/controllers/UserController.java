package com.wade.webofthings.controllers;

import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.user.PublicUser;
import com.wade.webofthings.models.user.User;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.dataset.parsers.UserResourceParser;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;

    @GetMapping("/users")
    ResponseEntity<List<PublicUser>> all(@RequestParam(required = false) String username) {
        return ResponseEntity.ok(UserResourceParser.getAllPublicUsers(dataset, model, username));
    }

    @GetMapping("/users/{id}")
    ResponseEntity<PublicUser> one(@PathVariable String id) {
        return ResponseEntity.ok(UserResourceParser.getPublicUserById(dataset, model, id));
    }

    @PostMapping("/users")
    ResponseEntity<PublicUser> newUser(@RequestBody User newUser) {
        dataset.begin(ReadWrite.WRITE) ;

        newUser.setId(String.valueOf(UUID.randomUUID()));
        String personURI = "/users/" + newUser.getId();

        Resource user = model.createResource(personURI)
                .addProperty(VCARD.UID, newUser.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.USER))
                .addProperty(VCARD.NICKNAME, newUser.getUsername())
                .addProperty(VCARD.KEY, newUser.getPassword());

        dataset.commit();

        return ResponseEntity.ok(new PublicUser(newUser.getId(), newUser.getUsername()));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        //remove all statements mentioning the home
        Resource user = model.getResource("/users/" + id);
        DatasetUtils.deleteResource(dataset, model, user);
    }

}
