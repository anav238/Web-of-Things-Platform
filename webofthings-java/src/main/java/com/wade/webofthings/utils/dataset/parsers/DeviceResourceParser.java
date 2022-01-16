package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.device.Device;
import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import com.wade.webofthings.utils.Constants.WOT;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.system.Txn;

import java.util.*;
import java.util.stream.Collectors;

public class DeviceResourceParser {

    public static List<Device> getAllDevices(Dataset dataset, Model model) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id ?title ?description ?property " +
                "WHERE { ?device  vcard:CLASS \"DEVICE\" . " +
                "?device vcard:UID ?id . " +
                "?device <" + WOT.DESCRIPTION + "> ?description . " +
                "?device <" + WOT.TITLE + "> ?title . " +
                "?device <" + WOT.HAS_PROPERTY_AFFORDANCE + "> ?property " +
                "}";

        Query query = QueryFactory.create(queryString) ;
        Map<String, Device> devices = new HashMap<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();

                    Literal id = soln.getLiteral("id");
                    Literal title = soln.getLiteral("title");
                    Literal description = soln.getLiteral("description");
                    Resource property = soln.getResource("property");

                    String idString = id != null? id.toString() : null;
                    String titleString = title != null? title.toString() : null;
                    String descriptionString = description != null? description.toString() : null;
                    String propertyString = property != null? property.toString() : null;

                    System.out.println(property);
                    System.out.println(soln);

                    DeviceProperty deviceProperty = new DeviceProperty(propertyString);
                    if (!devices.containsKey(idString)) {
                        List<DeviceProperty> deviceProperties = new ArrayList<>();
                        deviceProperties.add(deviceProperty);
                        devices.put(idString, new Device(idString, titleString, descriptionString, deviceProperties));
                    }
                    else {
                        Device device = devices.get(idString);
                        device.addProperty(deviceProperty);
                    }
                }
            }
        });

        return new ArrayList<>(devices.values());
    }
}
