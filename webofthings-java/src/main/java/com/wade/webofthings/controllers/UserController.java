package com.wade.webofthings.controllers;

import com.wade.webofthings.ApplicationData;
import com.wade.webofthings.models.User;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final ApplicationData applicationData = ApplicationData.getInstance();

    @GetMapping("/users")
    List<User> all() {
        Model model = applicationData.model;
        String queryString = "SELECT ?x " +
                "WHERE { ?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"John Smith\" }";
        Query query = QueryFactory.create(queryString) ;
        List<User> users = new ArrayList<>();
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                RDFNode x = soln.get("varName") ;       // Get a result variable by name.
                Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
                Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
                System.out.println(soln.toString());
                users.add(new User("test", "test", "test"));
            }
        }
        return users;
    }
}
