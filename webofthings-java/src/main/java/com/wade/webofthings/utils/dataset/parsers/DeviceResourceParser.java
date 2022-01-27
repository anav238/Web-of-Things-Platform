package com.wade.webofthings.utils.dataset.parsers;

import com.wade.webofthings.models.device.Device;
import com.wade.webofthings.utils.Constants.VocabularyConstants;
import com.wade.webofthings.utils.Constants.WOT;
import com.wade.webofthings.utils.mappers.DeviceActionMapper;
import com.wade.webofthings.utils.mappers.DevicePropertyMapper;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.system.Txn;

import java.util.ArrayList;
import java.util.List;

public class DeviceResourceParser {

    public static List<Device> getAllDevices(Dataset dataset, Model model) {
        List<String> deviceIds = getAllDeviceIds(dataset, model);
        System.out.println("device ids: " + deviceIds.toString());
        return getDevicesByIds(dataset, model, deviceIds);
    }

    private static List<String> getAllDeviceIds(Dataset dataset, Model model) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?id " +
                "WHERE { ?device  vcard:CLASS \"DEVICE\" . " +
                "?device vcard:UID ?id " +
                "}";

        Query query = QueryFactory.create(queryString);
        List<String> deviceIds = new ArrayList<>();
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    Literal id = soln.getLiteral("id");
                    String idString = id != null ? id.toString() : null;
                    if (idString != null)
                        deviceIds.add(idString);
                }
            }
        });
        return deviceIds;
    }

    public static List<Device> getDevicesByIds(Dataset dataset, Model model, List<String> ids) {
        List<Device> devices = new ArrayList<>();
        for (String id : ids) {
            devices.add(getDeviceById(dataset, model, id));
        }
        return devices;
    }

    public static Device getDeviceById(Dataset dataset, Model model, String id) {
        String queryString = VocabularyConstants.VCARD_PREFIX + " " +
                "SELECT ?title ?description ?property ?action " +
                //"?propertyName ?propertyDescription ?propertyMaximum ?property " +
                "WHERE { ?device  vcard:CLASS \"DEVICE\" . " +
                "?device vcard:UID \"" + id +  "\" . " +
                "?device <" + WOT.DESCRIPTION + "> ?description . " +
                "?device <" + WOT.TITLE + "> ?title . " +
                "OPTIONAL { ?device <" + WOT.HAS_PROPERTY_AFFORDANCE + "> ?property . " +
                "?device <" + WOT.HAS_ACTION_AFFORDANCE + "> ?action } " +
                "}";

        Query query = QueryFactory.create(queryString);
        Device device = new Device();
        device.setId(id);
        Txn.executeRead(dataset, () -> {
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();

                    Literal title = soln.getLiteral("title");
                    Literal description = soln.getLiteral("description");
                    Resource property = soln.getResource("property");
                    Resource action = soln.getResource("action");

                    String titleString = title != null ? title.toString() : null;
                    String descriptionString = description != null ? description.toString() : null;

                    System.out.println(property);
                    System.out.println(soln);

                    device.setTitle(titleString);
                    device.setDescription(descriptionString);
                    if (property != null)
                        device.addProperty(DevicePropertyMapper.mapResourceToDeviceProperty(property));
                    if (action != null)
                        device.addAction(DeviceActionMapper.mapResourceToDeviceAction(action));

                }
            }
        });

        return device;
    }
}
