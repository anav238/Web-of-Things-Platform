package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.Home;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.system.Txn;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

public class HomeResourceParser {

    public static List<Home> getAllHomes(Dataset dataset, Model model) {
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

    public static List<Home> getHomesByIds(Dataset dataset, Model model, List<String> ids) {
        List<Home> homes = new ArrayList<>();
        for (String id:ids) {
            homes.add(getHomeById(dataset, model, id));
        }
        return homes;
    }

    public static Home getHomeById(Dataset dataset, Model model, String id) {
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
}
