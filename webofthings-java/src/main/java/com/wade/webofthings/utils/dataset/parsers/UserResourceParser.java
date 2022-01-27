package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.home.Home;
import com.wade.webofthings.models.user.PublicUser;
import com.wade.webofthings.models.user.User;
import com.wade.webofthings.models.user.UserIdentity;
import com.wade.webofthings.models.user.UserRole;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.system.Txn;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserResourceParser {
    private static String SECRET_KEY ="Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5Ea";

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


        Query query = QueryFactory.create(queryString);
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
                System.out.println("getuserbyid "+soln);

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

    public static String Authenticate(Dataset dataset, Model model, String username, String password){
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id " +
                "WHERE { ?user vcard:NICKNAME \"" + username + "\" . " +
                "?user vcard:KEY \"" + password + "\" . " +
                "?user vcard:UID ?id" +
                "}";
        Query query = QueryFactory.create(queryString);
        User user = new User();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                QuerySolution soln = results.nextSolution();

                Literal idQuery = soln.getLiteral("id");

                user.setId(idQuery.toString());
                user.setUsername(username);
                user.setPassword(password);

            }
        });

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //int EXPIRATION_IN_SECONDS = 120000;
        //Date expiration=new Date(nowMillis+EXPIRATION_IN_SECONDS);

        String subject=user.getUsername();
        JwtBuilder builder = Jwts.builder().setId(user.getId())
                .setIssuedAt(now)
                //.setExpiration(expiration)
                .setSubject(subject)
                .setIssuer("Auth")
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);
        String jws=builder.compact();
        return jws;
    }

    public static UserIdentity Authorize(String jwt){
        if(jwt.length()<10)
            return UserIdentity.Unathorized();
        try{
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt.substring(7)).getBody();
            return new UserIdentity(true,claims, claims.getId());
        }
        catch(Exception e){
            return UserIdentity.Unathorized();
        }

    }

    public static String getUserRoleForHomeId(Dataset dataset, Model model, String homeId,String userId){
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                VocabularyConstants.VCARD4_PREFIX + " " +
                "SELECT ?homeId ?userRole ?userId" +
                "WHERE { ?user vcard:UID \"" + userId + "\" . " +
                "?user vcard:CLASS ?userRole . " +
                "?home vcard:UID ?homeId . " +
                "?home vcard4:hasMember ?user ." +
                "}";

        Query query = QueryFactory.create(queryString);
        User user=new User();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    System.out.println(soln);

                    Literal homeIdFromQuery = soln.getLiteral("homeId");
                    String homeIdString = homeIdFromQuery != null? homeIdFromQuery.toString() : null;


                    Literal userRole = soln.getLiteral("userRole");
                    String userRoleString = userRole != null? userRole.toString() : null;


                    boolean txt=    homeId.equals(homeId);
                    System.out.println(txt);

                    if(homeIdString.equals(homeId)) {
                        user.setUsername(userRoleString);
                    }

                }
            }
        });
        //In this case in username we have actually the role
        if(user.getUsername()!=null)
            return user.getUsername();
        return "";
    }

    public static List<Home> getUserHomes(Dataset dataset, Model model, String userId) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                VocabularyConstants.VCARD4_PREFIX + " " +
                "SELECT ?homeId ?userRole " +
                "WHERE { ?user vcard:UID \"" + userId + "\" . " +
                "?user vcard:CLASS ?userRole . " +
                "?home vcard:UID ?homeId . " +
                "?home vcard4:hasMember ?user " +
                "}";

        Query query = QueryFactory.create(queryString);
        Map<String, UserRole> homeIdsAndRoles = new HashMap<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    System.out.println(soln);

                    Literal homeId = soln.getLiteral("homeId");
                    String homeIdString = homeId != null? homeId.toString() : null;

                    Literal userRole = soln.getLiteral("userRole");
                    String userRoleString = userRole != null? userRole.toString() : null;

                    if (userRoleString != null && !userRoleString.equals("USER") && homeIdString != null) {
                        homeIdsAndRoles.put(homeIdString, UserRole.valueOf(userRoleString));
                    }
                }
            }
        });

        return HomeResourceParser.getHomesByIds(dataset, model, new ArrayList<>(homeIdsAndRoles.keySet()));
    }
}
