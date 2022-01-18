package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.user.PublicUser;
import com.wade.webofthings.models.user.User;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.system.Txn;

import java.util.ArrayList;
import java.util.List;

public class UserResourceParser {
    public static List<PublicUser> getAllPublicUsers(Dataset dataset, Model model, String usernameToSearch) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id ?username ?password " +
                "WHERE { ?user  vcard:CLASS \"USER\" . " +
                "?user vcard:UID ?id . " +
                "?user vcard:NICKNAME ?username ";

        if (usernameToSearch == null)
            queryString += "}";
        else
            queryString += ". FILTER regex(?username, \"" + usernameToSearch + "\", \"i\") }";


        Query query = QueryFactory.create(queryString) ;
        List<PublicUser> users = new ArrayList<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    System.out.println(soln);

                    Literal id = soln.getLiteral("id");
                    Literal username = soln.getLiteral("username");
                    users.add(new PublicUser(id.toString(), username.toString()));
                }
            }
        });
        return users;
    }

    public static User getUserById(Dataset dataset, Model model, String id) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id ?username ?password " +
                "WHERE { ?user vcard:UID \"" + id + "\" . " +
                "?user vcard:NICKNAME ?username . " +
                "?user vcard:KEY ?password " +
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

    public static PublicUser getPublicUserById(Dataset dataset, Model model, String id) {
        User user = getUserById(dataset, model, id);
        return new PublicUser(user.getId(), user.getUsername());
    }

}
