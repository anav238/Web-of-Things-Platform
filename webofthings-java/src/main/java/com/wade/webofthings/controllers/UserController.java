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
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        dataset.begin(ReadWrite.WRITE);
        newUser.setId(String.valueOf(UUID.randomUUID()));
        String personURI = "/users/" + newUser.getId();

        model.createResource(personURI)
                .addProperty(VCARD.UID, newUser.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.USER))
                .addProperty(VCARD.NICKNAME, newUser.getUsername())
                .addProperty(VCARD.KEY, newUser.getPassword());

        dataset.commit();

        return ResponseEntity.ok(new PublicUser(newUser.getId(), newUser.getUsername()));
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

    @PatchMapping(path = "/users/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<User> patchUser(@PathVariable String id, @RequestBody JsonPatch patch) {
        try {
            User user = UserResourceParser.getUserById(dataset, model, id);
            User userPatched = applyPatchUser(patch, user);

            System.out.println("user patched: " + userPatched.toString());

            UserResourceUpdater.updateUser(dataset, model, user, userPatched);
            return ResponseEntity.ok(userPatched);
            //deleteUser(id);
            //return newUserWithId(userPatched, id);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private User applyPatchUser(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    @GetMapping("/users/{userId}/homes")
    ResponseEntity<List<Home>> getUserHomes(@PathVariable String userId) {
        return ResponseEntity.ok(UserResourceParser.getUserHomes(dataset, model, userId));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable String id) {
        //remove all statements mentioning the home
        Resource user = model.getResource("/users/" + id);
        DatasetUtils.deleteResource(dataset, model, user);
    }

    @PostMapping("/users/authenticate")
    public ResponseEntity Authenticate(@RequestBody User newUser){
        try{
            String jws=UserResourceParser.Authenticate(dataset,model,newUser.getUsername(),newUser.getPassword());
            return ResponseEntity.ok(jws);
        }
        catch (Exception e)
        {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
