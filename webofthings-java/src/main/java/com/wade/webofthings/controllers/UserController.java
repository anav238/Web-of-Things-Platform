package com.wade.webofthings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.models.home.Home;
import com.wade.webofthings.models.user.PublicUser;
import com.wade.webofthings.models.user.User;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.dataset.parsers.UserResourceParser;
import com.wade.webofthings.utils.dataset.updaters.UserResourceUpdater;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JsonldModule());

    @RequestMapping(value = "/users", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> all(@RequestParam(required = false) String username) throws JsonProcessingException {
        //objectMapper.registerModule(new JsonldModule());
        List<PublicUser> publicUsers = UserResourceParser.getAllPublicUsers(dataset, model, username);
        return ResponseEntity.ok(objectMapper.writeValueAsString(publicUsers));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> one(@PathVariable String id) throws JsonProcessingException {
        try {
            //objectMapper.registerModule(new JsonldModule());
            PublicUser user = UserResourceParser.getPublicUserById(dataset, model, id);
            String userJsonLd = objectMapper.writeValueAsString(user);
            return ResponseEntity.ok(userJsonLd);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found");
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> newUser(@RequestBody User newUser) throws JsonProcessingException {
        dataset.begin(ReadWrite.WRITE);
        newUser.setId(String.valueOf(UUID.randomUUID()));
        String personURI = "/users/" + newUser.getId();

        model.createResource(personURI)
                .addProperty(VCARD.UID, newUser.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.USER))
                .addProperty(VCARD.NICKNAME, newUser.getUsername())
                .addProperty(VCARD.KEY, newUser.getPassword());

        dataset.commit();
        //objectMapper.registerModule(new JsonldModule());
        return ResponseEntity.ok(objectMapper.writeValueAsString(new PublicUser(newUser.getId(), newUser.getUsername())));
        //return ResponseEntity.ok(new PublicUser(newUser.getId(), newUser.getUsername()));
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PATCH,
            consumes = "application/json-patch+json", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> patchUser(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            User user = UserResourceParser.getUserById(dataset, model, id);
            User userPatched = applyPatchUser(patch, user);
            userPatched.setUri("/users/" + user.getId());
            System.out.println("user patched: " + userPatched.toString());

            UserResourceUpdater.updateUser(dataset, model, user, userPatched);
            //objectMapper.registerModule(new JsonldModule());
            return ResponseEntity.ok(objectMapper.writeValueAsString(userPatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private User applyPatchUser(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper patchMapper = new ObjectMapper();
        JsonNode patched = patch.apply(patchMapper.convertValue(targetUser, JsonNode.class));
        return patchMapper.treeToValue(patched, User.class);
    }

    @RequestMapping(value = "/users/{userId}/homes", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> getUserHomes(@PathVariable String userId) throws JsonProcessingException {
        //objectMapper.registerModule(new JsonldModule());
        return ResponseEntity.ok(objectMapper.writeValueAsString(UserResourceParser.getUserHomes(dataset, model, userId)));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        //remove all statements mentioning the user
        Resource user = model.getResource("/users/" + id);
        DatasetUtils.deleteResource(dataset, model, user);
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity Authenticate(@RequestBody User newUser) {
        try {
            String jws = UserResourceParser.Authenticate(dataset, model, newUser.getUsername(), newUser.getPassword());
            return ResponseEntity.ok(jws);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
