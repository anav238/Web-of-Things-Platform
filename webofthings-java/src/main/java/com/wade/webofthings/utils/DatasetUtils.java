package com.wade.webofthings.utils;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

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

    public static void deleteByQuery(Dataset dataset, Model model, String queryString) {
        dataset.begin(ReadWrite.WRITE);

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        UpdateAction.execute(updateRequest, dataset);

        dataset.commit();
    }

    public static String getStatementStringOrNull(Statement statement) {
        if (statement == null)
            return null;
        return statement.getString();
    }

}
