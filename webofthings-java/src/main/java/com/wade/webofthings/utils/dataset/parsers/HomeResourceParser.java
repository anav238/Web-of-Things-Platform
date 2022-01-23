package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.home.Home;
import com.wade.webofthings.models.home.HomeUserIdentifier;
import com.wade.webofthings.models.user.UserRole;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.system.Txn;
import org.apache.jena.vocabulary.VCARD;

import java.util.ArrayList;
import java.util.List;

public class HomeResourceParser {

    public static List<Home> getAllHomes(Dataset dataset, Model model) {
        List<String> homeIds = getAllHomeIds(dataset, model);
        System.out.println("home ids: " + homeIds.toString());
        return getHomesByIds(dataset, model, homeIds);
    }

    public static List<String> getAllHomeIds(Dataset dataset, Model model) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id " +
                "WHERE { ?home  vcard:CLASS \"HOME\" . " +
                "?home vcard:UID ?id " +
                "}";

        Query query = QueryFactory.create(queryString);
        List<String> homeIds = new ArrayList<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    Literal id = soln.getLiteral("id");
                    String idString = id != null ? id.toString() : null;
                    if (idString != null)
                        homeIds.add(idString);
                }
            }
        });
        return homeIds;
    }

    public static List<Home> getHomesByIds(Dataset dataset, Model model, List<String> ids) {
        List<Home> homes = new ArrayList<>();
        for (String id : ids) {
            homes.add(getHomeById(dataset, model, id));
        }
        return homes;
    }

    public static Home getHomeById(Dataset dataset, Model model, String id) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                VocabularyConstants.VCARD4_PREFIX + " " +
                "SELECT ?id ?name ?member " +
                "WHERE { ?home vcard:UID \"" + id + "\" . " +
                "?home vcard:NICKNAME ?name . " +
                "?home vcard4:hasMember ?member " +
                "}";

        Query query = QueryFactory.create(queryString);
        Home home = new Home();
        home.setId(id);
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();

                    Literal name = soln.getLiteral("name");
                    String nameString = name != null ? name.toString() : null;
                    home.setName(nameString);

                    if (soln.get("member").isResource()) {
                        Resource member = soln.getResource("member");

                        String currentUserId = member != null ? member.getProperty(VCARD.UID).getString() : null;
                        String currentUserRole = member != null ? member.getProperty(VCARD.CLASS).getString() : null;

                        System.out.println(soln);

                        if (currentUserId != null) {
                            HomeUserIdentifier homeUserIdentifier = new HomeUserIdentifier(currentUserId, UserRole.valueOf(currentUserRole));
                            home.addUserWithRole(homeUserIdentifier);
                        }
                    } else if (soln.get("member").isLiteral()) {
                        Literal member = soln.getLiteral("member");
                        String currentDeviceId = member != null ? member.toString() : null;
                        if (currentDeviceId != null)
                            home.addDeviceId(currentDeviceId);
                    }
                }
            }
        });

        return home;
    }
}
