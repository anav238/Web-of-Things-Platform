package com.wade.webofthings.utils.mappers;

import com.wade.webofthings.models.device.DeviceProperty;
import com.wade.webofthings.utils.Constants.WOT;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;

public class DevicePropertyMapper {
    public static Resource mapToResource(Model model, DeviceProperty deviceProperty) {
        return model.createResource().addProperty(new PropertyImpl(WOT.NAME), deviceProperty.getName());
    }

}
