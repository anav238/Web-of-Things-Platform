package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.device.Device;
import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.utils.Constants.Schema;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.system.Txn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceResourceParser {

    public static List<Device> getAllDevices(Dataset dataset, Model model) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id ?title ?description ?property " +
                //"?propertyName ?propertyDescription ?propertyMaximum ?property " +
                "WHERE { ?device  vcard:CLASS \"DEVICE\" . " +
                "?device vcard:UID ?id . " +
                "?device <" + WOT.DESCRIPTION + "> ?description . " +
                "?device <" + WOT.TITLE + "> ?title . " +
                "?device <" + WOT.HAS_PROPERTY_AFFORDANCE + "> ?property " +
                /*"?property <" + WOT.NAME + "> ?propertyName . " +
                "?property <" + WOT.DESCRIPTION + "> ?propertyDescription . " +
                "?property <" + Schema.MAXIMUM + "> ?propertyMaximum . " +
                "?property <" + Schema.MINIMUM + "> ?propertyMinimum . " +
                "?property <" + Schema.UNIT_TEXT + "> ?propertyUnit . " +
                "?property <" + Schema.TYPE + "> ?propertyType . " +
                "?property <" + Schema.VALUE_TYPE + "> ?propertyValueType . " +
                "?property <" + Schema.VALUE + "> ?propertyValue " +*/
                "}";

        Query query = QueryFactory.create(queryString);
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

                    String idString = id != null ? id.toString() : null;
                    String titleString = title != null ? title.toString() : null;
                    String descriptionString = description != null ? description.toString() : null;

                    System.out.println(property);
                    System.out.println(soln);

                    if (!devices.containsKey(idString)) {
                        List<DeviceProperty> deviceProperties = new ArrayList<>();
                        if (property != null)
                            deviceProperties.add(DevicePropertyMapper.mapResourceToDeviceProperty(property));
                        devices.put(idString, new Device(idString, titleString, descriptionString, deviceProperties));
                    } else {
                        Device device = devices.get(idString);
                        if (property != null)
                            device.addProperty(DevicePropertyMapper.mapResourceToDeviceProperty(property));
                    }
                }
            }
        });

        return new ArrayList<>(devices.values());
    }
}
