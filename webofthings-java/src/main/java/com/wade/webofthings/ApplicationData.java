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
        //dataset.begin(ReadWrite.READ);

        model = dataset.getDefaultModel();
        /*dataset.end() ;
        dataset.begin(ReadWrite.WRITE) ;

        String personURI    = "http://somewhere/JohnSmith";
        String givenName    = "John";
        String familyName   = "Smith";
        String fullName     = givenName + " " + familyName;

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith
                = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,
                        model.createResource()
                                .addProperty(VCARD.Given, givenName)
                                .addProperty(VCARD.Family, familyName));

        model.write(System.out);

        String queryString = "SELECT ?x " +
                "WHERE { ?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"John Smith\" }";
        Query query = QueryFactory.create(queryString) ;
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                RDFNode x = soln.get("varName") ;       // Get a result variable by name.
                Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
                Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
                System.out.println(soln.toString());
            }
        }
        dataset.commit();
        dataset.end() ;*/
    }

    public static ApplicationData getInstance()
    {
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
