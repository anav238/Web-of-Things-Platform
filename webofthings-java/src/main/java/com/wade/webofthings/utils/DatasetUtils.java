package com.wade.webofthings.utils;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public class DatasetUtils {
    public void postResource(Dataset dataset, Model model, Resource resource) {
        dataset.begin(ReadWrite.WRITE);
        model.createResource(resource);
        dataset.commit();
    }
}
