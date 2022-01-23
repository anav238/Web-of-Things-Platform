package com.wade.webofthings.utils.mappers;

import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.models.home.HomeUserIdentifier;
import com.wade.webofthings.utils.Constants.WOT;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.vocabulary.VCARD;

public class HomeUserIdentifierMapper {
    public static Resource mapToResource(Model model, HomeUserIdentifier homeUserIdentifier) {
        return model.createResource().addProperty(VCARD.UID, homeUserIdentifier.getUserId())
                .addProperty(VCARD.CLASS, homeUserIdentifier.getUserRole().toString());
    }

    /*public static DeviceProperty mapResourceToDeviceProperty(Model model, Resource resource) {
        //Resource resource = model.createResource().addProperty(new PropertyImpl(WOT.NAME), deviceProperty.getName());
        //return resource;
    }*/
}
