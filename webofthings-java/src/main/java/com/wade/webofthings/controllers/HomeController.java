package com.wade.webofthings.controllers;

import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.Home;
import com.wade.webofthings.models.User;
import com.wade.webofthings.models.enums.ResourceType;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.system.Txn;
import org.apache.jena.vocabulary.VCARD;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class HomeController {
    private ApplicationData applicationData = ApplicationData.getInstance();

    @GetMapping("/homes")
    List<Home> all() {
        Dataset dataset = applicationData.dataset;
        Model model = applicationData.model;

        String queryString = "SELECT ?id ?name " +
                "WHERE { ?home  <http://www.w3.org/2001/vcard-rdf/3.0#CLASS> \"HOME\" . " +
                "?home <http://www.w3.org/2001/vcard-rdf/3.0#UID> ?id . " +
                "?home <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME> ?name " +
                "}";

        Query query = QueryFactory.create(queryString) ;
        List<Home> homes = new ArrayList<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();

                    Literal id = soln.getLiteral("id");
                    Literal name = soln.getLiteral("name");

                    System.out.println(soln);
                    homes.add(new Home(id.toString(), name.toString()));
                }
            }
        });
        return homes;
    }

    @GetMapping("/homes/{id}")
    Home one(@PathVariable String id) {
        Dataset dataset = applicationData.dataset;
        Model model = applicationData.model;

        String queryString = "SELECT ?id ?name " +
                "WHERE { ?home <http://www.w3.org/2001/vcard-rdf/3.0#UID> \"" + id + "\" . " +
                "?home <http://www.w3.org/2001/vcard-rdf/3.0#NICKNAME> ?name " +
                "}";

        Query query = QueryFactory.create(queryString);
        Home home = new Home();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                QuerySolution soln = results.nextSolution();

                Literal name = soln.getLiteral("name");

                System.out.println(soln);
                home.setId(id);
                home.setName(name.toString());
            }
        });

        return home;
    }

    @PostMapping("/homes")
    Home newHome(@RequestBody Home newHome) {
        Dataset dataset = applicationData.dataset;
        Model model = applicationData.model;

        dataset.begin(ReadWrite.WRITE) ;

        newHome.setId(String.valueOf(UUID.randomUUID()));
        String homeURI = "/homes/" + newHome.getId();

        Resource home = model.createResource(homeURI)
                .addProperty(VCARD.UID, newHome.getId())
                .addProperty(VCARD.CLASS, String.valueOf(ResourceType.HOME))
                .addProperty(VCARD.NICKNAME, newHome.getName());

        dataset.commit();

        return newHome;
    }
}
