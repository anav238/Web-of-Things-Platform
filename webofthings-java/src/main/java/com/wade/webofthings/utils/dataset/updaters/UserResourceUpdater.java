package com.wade.webofthings.utils.dataset.updaters;

import com.wade.webofthings.models.user.User;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.VCARD;

public class UserResourceUpdater {

    public static void updateUser(Dataset dataset, Model model, User oldUser, User newUser) {
        dataset.begin(ReadWrite.WRITE);

        Resource userResource = model.getResource("/users/" + oldUser.getId());
        if (!oldUser.getUsername().equals(newUser.getUsername())) {
            model.removeAll(userResource, VCARD.NICKNAME, null);
            model.add(userResource, VCARD.NICKNAME, newUser.getUsername());
        }

        if (!oldUser.getPassword().equals(newUser.getPassword())) {
            model.removeAll(userResource, VCARD.KEY, null);
            model.add(userResource, VCARD.KEY, newUser.getPassword());
        }

        dataset.commit();
    }

}
