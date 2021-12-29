package com.wade.webofthings.controllers;

import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.User;
import com.wade.webofthings.models.enums.ResourceType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.system.Txn;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private final ApplicationData applicationData = ApplicationData.getInstance();

    @GetMapping("/users")
    List<User> all() {
        Dataset dataset = applicationData.dataset;
        Model model = applicationData.model;

        String queryString = "SELECT ?id ?username ?password " +
                "WHERE { ?user  <http://www.w3.org/2001/vcard-rdf/3.0#CLASS> \"USER\" . " +
                "?user <http://www.w3.org/2001/vcard-rdf/3.0#UID> ?id . " +
                "?user <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME> ?username . " +
                "?user <http://www.w3.org/2001/vcard-rdf/3.0#KEY> ?password " +
                "}";

        Query query = QueryFactory.create(queryString) ;
        List<User> users = new ArrayList<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();

                    Literal id = soln.getLiteral("id");
                    Literal username = soln.getLiteral("username");
                    Literal password = soln.getLiteral("password");

                    System.out.println(soln);
                    users.add(new User(id.toString(), username.toString(), password.toString()));
                }
            }
        });
        return users;
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable String id) {
        Dataset dataset = applicationData.dataset;
        Model model = applicationData.model;

        String queryString = "SELECT ?id ?username ?password " +
                "WHERE { ?user <http://www.w3.org/2001/vcard-rdf/3.0#UID> \"" + id + "\" . " +
                "?user <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME> ?username . " +
                "?user <http://www.w3.org/2001/vcard-rdf/3.0#KEY> ?password " +
                "}";

        Query query = QueryFactory.create(queryString);
        User user = new User();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                QuerySolution soln = results.nextSolution();

                Literal username = soln.getLiteral("username");
                Literal password = soln.getLiteral("password");

                System.out.println(soln);
                user.setId(id);
                user.setUsername(username.toString());
                user.setPassword(password.toString());
            }
        });

        return user;
    }

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        Dataset dataset = applicationData.dataset;
        Model model = applicationData.model;

        dataset.begin(ReadWrite.WRITE) ;

        newUser.setId(String.valueOf(UUID.randomUUID()));
        String personURI = "/users/" + newUser.getId();

        Resource user = model.createResource(personURI)
                .addProperty(VCARD.UID, newUser.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.USER))
                .addProperty(VCARD.NICKNAME, newUser.getUsername())
                .addProperty(VCARD.KEY, newUser.getPassword());

        dataset.commit();

        return newUser;
    }
}
