package com.wade.webofthings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.ResourceType;
import com.wade.webofthings.models.home.Home;
import com.wade.webofthings.models.home.HomeUserIdentifier;
import com.wade.webofthings.models.user.ChangeHomeRoleRequest;
import com.wade.webofthings.models.user.UserIdentity;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.dataset.parsers.HomeResourceParser;
import com.wade.webofthings.utils.dataset.parsers.UserResourceParser;
import com.wade.webofthings.utils.dataset.updaters.HomeResourceUpdater;
import com.wade.webofthings.utils.mappers.HomeUserIdentifierMapper;
import ioinformarics.oss.jackson.module.jsonld.JsonldModule;
import jakarta.json.JsonObject;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.NotFoundException;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.VCARD4;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class HomeController {
    private final ApplicationData applicationData = ApplicationData.getInstance();
    private final Dataset dataset = applicationData.dataset;
    private final Model model = applicationData.model;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JsonldModule());

    @RequestMapping(value = "/homes", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> all( @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        return ResponseEntity.ok(objectMapper.writeValueAsString(HomeResourceParser.getAllHomes(dataset, model)));
    }

    @RequestMapping(value = "/homes/{id}", method = RequestMethod.GET,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> one(@PathVariable String id, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForHomeId(dataset, model, id, identity.getUserId());
        if (role.equals("OWNER")) {
            try {
                return ResponseEntity.ok(objectMapper.writeValueAsString(HomeResourceParser.getHomeById(dataset, model, id)));
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Home not Found");
            }
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @RequestMapping(value = "/homes", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    ResponseEntity<String> newHome(@RequestBody Home newHome, @RequestHeader(value = "authorization") String jwt) throws JsonProcessingException {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        String randomId = String.valueOf(UUID.randomUUID());
        return ResponseEntity.ok(objectMapper.writeValueAsString(newHomeWithId(newHome, randomId)));
    }

    private Home newHomeWithId(Home newHome, String id) {
        dataset.begin(ReadWrite.WRITE);

        newHome.setId(id);
        String homeURI = "/homes/" + newHome.getId();

        Resource homeResource = model.createResource(homeURI)
                .addProperty(VCARD.UID, newHome.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.HOME))
                .addProperty(VCARD.NICKNAME, newHome.getName());

        for (HomeUserIdentifier homeUserIdentifier : newHome.getUsers())
            homeResource.addProperty(VCARD4.hasMember, HomeUserIdentifierMapper.mapToResource(model, homeUserIdentifier));

        for (String deviceId : newHome.getDeviceIds())
            homeResource.addProperty(VCARD4.hasRelated, deviceId);

        dataset.commit();

        return newHome;
    }

    @RequestMapping(value = "/homes/{id}", method = RequestMethod.PATCH,
            consumes = "application/json-patch+json", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> patchHome(@PathVariable String id, @RequestBody JsonPatch patch,  @RequestHeader(value = "authorization") String jwt) {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        String role = UserResourceParser.getUserRoleForHomeId(dataset, model, id, identity.getUserId());
        if (role.equals("OWNER")) {
            try {
                    Home home = HomeResourceParser.getHomeById(dataset, model, id);
                    Home homePatched = applyPatchToHome(patch, home);
                    System.out.println("home before patch: " + home.toString());
                    System.out.println("patched home: " + homePatched.toString());

                    HomeResourceUpdater.updateHome(dataset, model, home, homePatched);
                    return ResponseEntity.ok(objectMapper.writeValueAsString(homePatched));

                } catch (NotFoundException e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found");
                } catch (JsonPatchException | JsonProcessingException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
         else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @RequestMapping(value = "/homes/{homeId}/users/{userId}", method = RequestMethod.PATCH,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<JsonObject> patchHome(@PathVariable String homeId, @PathVariable String userId, @RequestBody ChangeHomeRoleRequest request, @RequestHeader(value = "authorization") String jwt) {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

        String role = UserResourceParser.getUserRoleForHomeId(dataset, model, homeId, identity.getUserId());
        if (role.equals("OWNER")) {
            try {
                HomeResourceUpdater.updateHomeUserRole(dataset, model, homeId, userId, request.getRole());
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Home not Found");
            }
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    private Home applyPatchToHome(JsonPatch patch, Home targetHome) throws JsonPatchException, JsonProcessingException {
        ObjectMapper patchMapper = new ObjectMapper();
        JsonNode patched = patch.apply(patchMapper.convertValue(targetHome, JsonNode.class));
        return patchMapper.treeToValue(patched, Home.class);
    }

    @DeleteMapping("/homes/{id}")
    public void deleteHome(@PathVariable String id, @RequestHeader(value = "authorization") String jwt) {
        UserIdentity identity = UserResourceParser.Authorize(jwt);
        if (!identity.isAuthorized())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        String role = UserResourceParser.getUserRoleForHomeId(dataset, model, id, identity.getUserId());
        if (role.equals("OWNER")) {
            try {
                //remove all statements mentioning the home
                Resource home = model.getResource("/homes/" + id);
                DatasetUtils.deleteResource(dataset, model, home);
            }
            catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Home not Found");
            }
        } else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

    }
}
