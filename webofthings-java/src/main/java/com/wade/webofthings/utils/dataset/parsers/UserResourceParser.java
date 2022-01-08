package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.User;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.system.Txn;

import java.util.ArrayList;
import java.util.List;

public class UserResourceParser {
    public static List<User> getAllUsers(Dataset dataset, Model model) {
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

    public static User getUserById(Dataset dataset, Model model, String id) {
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
}
