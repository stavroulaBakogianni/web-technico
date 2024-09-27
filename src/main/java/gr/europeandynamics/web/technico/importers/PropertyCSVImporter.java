package gr.europeandynamics.web.technico.importers;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.PropertyType;
import gr.europeandynamics.web.technico.models.User;
import gr.europeandynamics.web.technico.services.PropertyServiceImpl;
import gr.europeandynamics.web.technico.services.UserServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@ApplicationScoped
public class PropertyCSVImporter implements FilesImporter {

    @Inject
    private UserServiceImpl userService;

    @Inject
    private PropertyServiceImpl propertyService;

    /**
     * Imports property data from the specified CSV file. The method reads the file,
     * parses each line, and calls {@code propertyService.createProperty()} to
     * store valid property entries.
     *
     * If the line is malformed, such as an invalid number format or unknown property type,
     * the importer skips the line.
     *
     * @param filePath the path to the CSV file to be imported
     */
    @Override
    public void importFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                PropertyCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length != 5) {
                    //The line is malformed, skip it
                    continue;
                }

                String e9 = fields[0];
                String propertyAddress = fields[1];

                int constructionYear;
                try {
                    constructionYear = Integer.parseInt(fields[2]);
                } catch (NumberFormatException e) {
                    // Invalid CSV format: construction year must be an integer, skip line
                    continue;
                }
                PropertyType propertyType;
                try {
                    propertyType = PropertyType.valueOf(fields[3]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid property type, skip line
                    continue;
                }
                String userVat = fields[4];

                Optional<User> userOptional = userService.getUserByVat(userVat);
                if (!userOptional.isPresent()) {
                    // Owner not found, skip line
                    continue;
                }
                User user = userOptional.get();

                propertyService.createProperty(e9, propertyAddress, constructionYear, propertyType, user.getVat());
            }
        } catch (OutOfMemoryError e) {
            System.out.println("Java run out of memory: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Filepath not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
