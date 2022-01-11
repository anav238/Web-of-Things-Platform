package com.wade.webofthings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.User;
import com.wade.webofthings.models.enums.ResourceType;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.dataset.parsers.UserResourceParser;
import org.apache.jena.atlas.json.JSON;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/users")
    ResponseEntity<List<User>> all() {
        return ResponseEntity.ok(UserResourceParser.getAllUsers(dataset, model));
    }

    @GetMapping("/users/{id}")
    ResponseEntity<User> one(@PathVariable String id) {
        return ResponseEntity.ok(UserResourceParser.getUserById(dataset, model, id));
    }

    @PostMapping("/users")
    ResponseEntity<User> newUser(@RequestBody User newUser) {
        dataset.begin(ReadWrite.WRITE) ;

        newUser.setId(String.valueOf(UUID.randomUUID()));
        String personURI = "/users/" + newUser.getId();

        Resource user = model.createResource(personURI)
                .addProperty(VCARD.UID, newUser.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.USER))
                .addProperty(VCARD.NICKNAME, newUser.getUsername())
                .addProperty(VCARD.KEY, newUser.getPassword());

        dataset.commit();

        return ResponseEntity.ok(newUser);
    }

    ResponseEntity<User> newUserWithId(User newUser, String id) {
        dataset.begin(ReadWrite.WRITE);

        newUser.setId(id);
        String personURI = "/users/" + newUser.getId();

        model.createResource(personURI)
                .addProperty(VCARD.UID, newUser.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.USER))
                .addProperty(VCARD.NICKNAME, newUser.getUsername())
                .addProperty(VCARD.KEY, newUser.getPassword());

        dataset.commit();

        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        //remove all statements mentioning the home
        Resource user = model.getResource("/users/" + id);
        DatasetUtils.deleteResource(dataset, model, user);
    }

    @PatchMapping(path  = "/users/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<User> patchUser(@PathVariable String id, @RequestBody JsonPatch patch){
        try{
            User user = UserResourceParser.getUserById(dataset,model,id);
            User userPatched = applyPatchUser(patch, user);
            //Deleting user from database
            deleteUser(id);
            newUserWithId(userPatched, id);

            return ResponseEntity.ok(userPatched);
        }
        catch(JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private User applyPatchUser(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        JsonNode patched =  patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

}
