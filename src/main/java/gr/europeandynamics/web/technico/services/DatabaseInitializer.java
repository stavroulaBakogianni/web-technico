package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.importers.PropertyCSVImporter;
import gr.europeandynamics.web.technico.importers.RepairCSVImporter;
import gr.europeandynamics.web.technico.importers.UserCSVImporter;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

/**
 * This class is responsible for initializing the database by importing data 
 * from CSV files. The imports are performed during the startup of the 
 * application.
 */
@Startup
@Singleton
public class DatabaseInitializer {

    @Inject
    private UserCSVImporter userCSVImporter;

    @Inject
    private PropertyCSVImporter propertyCSVImporter;

    @Inject
    private RepairCSVImporter repairCSVImporter;

     /**
     * This method is called after the object has been created and its 
     * dependencies have been injected. It triggers the import process 
     * for user, property, and repair data by calling the respective 
     * importer methods with the paths to the CSV files.
     */
    @PostConstruct
    public void init() {
        userCSVImporter.importFile("csv_files/users.csv");
        propertyCSVImporter.importFile("csv_files/properties.csv");
        repairCSVImporter.importFile("csv_files/repairs.csv");
    }
}
