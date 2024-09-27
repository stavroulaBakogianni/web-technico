package gr.europeandynamics.web.technico.importers;

import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.Repair;
import gr.europeandynamics.web.technico.models.RepairStatus;
import gr.europeandynamics.web.technico.models.RepairType;
import gr.europeandynamics.web.technico.repositories.RepairRepositoryImpl;
import gr.europeandynamics.web.technico.services.PropertyServiceImpl;
import gr.europeandynamics.web.technico.services.UserServiceImpl;
import jakarta.inject.Inject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class RepairCSVImporter implements FilesImporter {

    @Inject
    private UserServiceImpl userService;

    @Inject
    private PropertyServiceImpl propertyService;

    @Inject
    private RepairRepositoryImpl repairRepository;

    /**
     * Imports the repair data from a specified CSV file. This method reads the 
     * file, parses each line, validates the data, and saves it to the repository. 
     * If any line is malformed or contains invalid data, it is skipped.
     * 
     * @param filePath the path to the CSV file to be imported
     */
    @Override
    public void importFile(String filePath) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                PropertyCSVImporter.class.getClassLoader().getResourceAsStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (!(fields.length >= 10 && fields.length <= 12)) {
                    //The line is malformed, skip it
                    continue;
                }

                Long propertyId;
                try {
                    propertyId = Long.parseLong(fields[0]);
                } catch (NumberFormatException e) {
                    // Invalid CSV format: propertyId must be Long, skip line
                    continue;
                }

                RepairType repairType;
                try {
                    repairType = RepairType.valueOf(fields[1]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid repair type, skip line
                    continue;
                }

                String shortDiscription = fields[2];

                LocalDateTime submitionDate;
                try {
                    submitionDate = LocalDateTime.parse(fields[3], formatter);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid date format, skip line
                    continue;
                }

                String description = fields[4];
                LocalDateTime proposedStartDate;
                try {
                    proposedStartDate = LocalDateTime.parse(fields[5], formatter);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid date format, skip line
                    continue;
                }

                LocalDateTime proposedEndDate;
                try {
                    proposedEndDate = LocalDateTime.parse(fields[6], formatter);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid date format, skip line
                    continue;
                }

                BigDecimal proposedCost;
                try {
                    proposedCost = new BigDecimal(fields[7]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid cost format, skip line
                    continue;
                }

                boolean acceptanceStatus;
                try {
                    acceptanceStatus = Boolean.parseBoolean(fields[8]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid status format, skip line
                    continue;
                }

                RepairStatus repairStatus;
                try {
                    repairStatus = RepairStatus.valueOf(fields[9]);
                } catch (IllegalArgumentException e) {
                    // Invalid CSV format: invalid repair status, skip line
                    continue;
                }

                LocalDateTime actualStartDate = null;
                if (fields.length >= 11) {
                    try {
                        actualStartDate = LocalDateTime.parse(fields[10], formatter);
                    } catch (IllegalArgumentException e) {
                        // Invalid CSV format: invalid date format, skip line
                        continue;
                    }
                }

                LocalDateTime actualEndDate = null;
                if (fields.length == 12) {
                    try {
                        actualEndDate = LocalDateTime.parse(fields[11], formatter);
                    } catch (IllegalArgumentException e) {
                        // Invalid CSV format: invalid date format, skip line
                        continue;
                    }
                }

                Optional<Property> propertyOptional = propertyService.findPropertyByID(propertyId);
                if (!propertyOptional.isPresent()) {
                    // Property not found, skip line
                    continue;
                }
                Property property = propertyOptional.get();

                Repair repair = new Repair();
                repair.setProperty(property);
                repair.setRepairType(repairType);
                repair.setShortDescription(shortDiscription);
                repair.setSubmissionDate(submitionDate);
                repair.setDescription(description);
                repair.setProposedStartDate(proposedStartDate);
                repair.setProposedEndDate(proposedEndDate);
                repair.setProposedCost(proposedCost);
                repair.setAcceptanceStatus(acceptanceStatus);
                repair.setRepairStatus(repairStatus);
                repair.setActualStartDate(actualStartDate);
                repair.setActualEndDate(actualEndDate);

                repairRepository.save(repair);
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
