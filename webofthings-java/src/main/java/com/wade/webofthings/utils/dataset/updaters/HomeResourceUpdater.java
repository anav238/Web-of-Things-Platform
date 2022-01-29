package com.wade.webofthings.utils.dataset.updaters;

import com.wade.webofthings.models.home.Home;
import com.wade.webofthings.models.home.HomeUserIdentifier;
import com.wade.webofthings.models.user.UserRole;
import com.wade.webofthings.utils.DatasetUtils;
import com.wade.webofthings.utils.constants.VocabularyConstants;
import com.wade.webofthings.utils.mappers.HomeUserIdentifierMapper;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.VCARD4;

public class HomeResourceUpdater {

    public static void updateHome(Dataset dataset, Model model, Home oldHome, Home newHome) {
        Resource homeResource = model.getResource("/homes/" + oldHome.getId());
        if (!oldHome.getName().equals(newHome.getName())) {
            dataset.begin(ReadWrite.WRITE);
            model.removeAll(homeResource, VCARD.NICKNAME, null);
            model.add(homeResource, VCARD.NICKNAME, newHome.getName());
            dataset.commit();
        }

        if (!oldHome.getDeviceIds().equals(newHome.getDeviceIds())) {
            String deleteQueryString = VocabularyConstants.VCARD_PREFIX + " " +
                    VocabularyConstants.VCARD4_PREFIX + " " +
                    "DELETE { ?home vcard4:hasRelated ?device " +
                    "} WHERE { ?home vcard4:hasRelated ?device . " +
                    "?home vcard:UID \"" + oldHome.getId() + "\"} ";

            DatasetUtils.deleteByQuery(dataset, model, deleteQueryString);

            dataset.begin(ReadWrite.WRITE);
            for (String deviceId : newHome.getDeviceIds())
                homeResource.addProperty(VCARD4.hasRelated, deviceId);
            dataset.commit();
        }

        if (!oldHome.getUsers().equals(newHome.getUsers())) {
            String deleteQueryString = VocabularyConstants.VCARD_PREFIX + " " +
                    VocabularyConstants.VCARD4_PREFIX + " " +
                    "DELETE { ?blank_node vcard:UID ?userId . " +
                    "?blank_node vcard:CLASS ?userRole . " +
                    "?home vcard4:hasMember ?blank_node " +
                    "} WHERE { ?home vcard4:hasMember ?blank_node . " +
                    "?home vcard:UID \"" + oldHome.getId() + "\"} ";

            DatasetUtils.deleteByQuery(dataset, model, deleteQueryString);

            dataset.begin(ReadWrite.WRITE);
            for (HomeUserIdentifier homeUserIdentifier : newHome.getUsers())
                homeResource.addProperty(VCARD4.hasMember, HomeUserIdentifierMapper.mapToResource(model, homeUserIdentifier));
            dataset.commit();
        }
    }

    public static void updateHomeUserRole(Dataset dataset, Model model, String homeId, String userId, UserRole newUserRole) {
        String deleteQueryString = VocabularyConstants.VCARD_PREFIX + " " +
                VocabularyConstants.VCARD4_PREFIX + " " +
                "DELETE { ?blank_node vcard:UID ?userId . " +
                "?blank_node vcard:CLASS ?userRole . " +
                "?home vcard4:hasMember ?blank_node " +
                "} WHERE { ?home vcard4:hasMember ?blank_node . " +
                "?home vcard:UID \"" + homeId + "\" . " +
                "?blank_node vcard:UID \"" + userId + "\"} ";

        DatasetUtils.deleteByQuery(dataset, model, deleteQueryString);

        Resource homeResource = model.getResource("/homes/" + homeId);
        dataset.begin(ReadWrite.WRITE);
        homeResource.addProperty(VCARD4.hasMember, HomeUserIdentifierMapper.mapToResource(model, new HomeUserIdentifier(userId, newUserRole)));
        dataset.commit();
    }

}
