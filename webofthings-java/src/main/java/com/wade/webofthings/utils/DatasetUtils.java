package com.wade.webofthings.utils;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class DatasetUtils {

    public static void postResource(Dataset dataset, Model model, Resource resource) {
        dataset.begin(ReadWrite.WRITE);
        model.createResource(resource);
        dataset.commit();
    }

    public static void deleteResource(Dataset dataset, Model model, Resource resource) {
        dataset.begin(ReadWrite.WRITE);

        // remove statements where resource is subject
        model.removeAll(resource, null, (RDFNode) null);
        // remove statements where resource is object
        model.removeAll(null, null, resource);

        dataset.commit();
    }
}
