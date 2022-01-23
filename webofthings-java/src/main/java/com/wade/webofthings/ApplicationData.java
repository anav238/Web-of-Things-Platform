package com.wade.webofthings;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ApplicationData {
    private static ApplicationData singleInstance = null;

    private String dataDirectory = "Dataset2";
    public Dataset dataset;
    public Model model;

    private ApplicationData() {
        dataset = TDBFactory.createDataset(dataDirectory);
        model = dataset.getDefaultModel();
    }

    public static ApplicationData getInstance() {
        if (singleInstance == null)
            singleInstance = new ApplicationData();

        return singleInstance;
    }

    @PreDestroy
    public void destroy() {
        dataset.end();
        System.out.println(
                "Callback triggered - @PreDestroy.");
    }
}
