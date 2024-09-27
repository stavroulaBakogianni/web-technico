package gr.europeandynamics.web.technico.importers;

import gr.europeandynamics.web.technico.services.UserServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

@ApplicationScoped
public class UserCSVImporter implements FilesImporter {

    @Inject
    private UserServiceImpl userService;

     /**
     * Imports user data from the specified CSV file. The method reads the file, 
     * parses each line, and calls the {@code userService.createUser()} method 
     * to store valid user entries. It expects the CSV file to contain exactly 
     * 8 fields per line.
     * 
     * If the line is malformed or the file is not found, the method handles 
     * the error and continues with the next line or operation.
     * 
     * @param filePath the path to the CSV file to be imported
     */
    @Override
    public void importFile(String filePath) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                UserCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] fields = line.split(",");

                if (fields.length != 8) {
                    //The line is malformed, skip it
                    continue;
                }

                String vat = fields[0];
                String name = fields[1];
                String surname = fields[2];
                String address = fields[3];
                String phoneNumber = fields[4];
                String email = fields[5];
                String password = fields[6];

                userService.createUser(vat, name, surname, address, phoneNumber, email, password);
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
