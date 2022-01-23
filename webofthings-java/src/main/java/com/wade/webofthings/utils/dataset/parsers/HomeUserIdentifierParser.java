package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.home.HomeUserIdentifier;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;

public class HomeUserIdentifierParser {
    /*public HomeUserIdentifier getHomeUserIdentifierById(Dataset dataset, Model model, String id) {
        model.getResource(id);
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id ?userId ?userRole " +
                "WHERE { ?homeUserIdentifier " +
                "?homeUserIdentifier  vcard:CLASS ?userRole . " +
                "?device vcard:UID ?id . " +
                "}";


    }*/
}
