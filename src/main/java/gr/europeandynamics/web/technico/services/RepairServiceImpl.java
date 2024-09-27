package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.Repair;
import gr.europeandynamics.web.technico.models.RepairType;
import gr.europeandynamics.web.technico.repositories.RepairRepositoryImpl;
import gr.europeandynamics.web.technico.validators.RepairValidator;
import gr.europeandynamics.web.technico.models.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class RepairServiceImpl implements RepairService {

    @Inject
    private RepairRepositoryImpl repairRepository;

    @Inject
    private RepairValidator repairValidator;

    /**
     * Creates a new Repair with the provided details.
     *
     * @param repairType the type of the repair
     * @param shortDescription a brief description of the repair
     * @param description a detailed description of the repair
     * @param property the Property associated with the repair
     * @return an Optional containing the created Repair if successful, or an
     * empty Optional if an error occurs
     */
    @Override
    @Transactional
    public Optional<Repair> createRepair(RepairType repairType, String shortDescription, String description, Property property) {
        try {
            Repair repair = new Repair();
            repair.setRepairType(repairType);
            repair.setShortDescription(shortDescription);
            repair.setDescription(description);
            repair.setProperty(property);
            repair.setSubmissionDate(LocalDateTime.now());

            repairValidator.validateRepair(repair);

            Optional<Repair> savedRepair = repairRepository.save(repair);
            return savedRepair;
        } catch (CustomException e) {
            log.error("Error creating repair: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Updates the details of an existing Repair.
     *
     * @param repair the Repair entity with updated details
     * @return an Optional containing the updated Repair if successful, or an
     * empty Optional if the repair does not exist
     */
    @Override
    @Transactional
    public Optional<Repair> updateRepair(Repair repair) {
        try {
            repairValidator.validateRepair(repair);

            Optional<Repair> existingRepairOptional = repairRepository.getById(repair.getId());
            if (existingRepairOptional.isEmpty()) {
                log.error("Repair with ID {} not found.", repair.getId());
                throw new CustomException("Repair with ID " + repair.getId() + " not found.");
            }
            Repair existingRepair = existingRepairOptional.get();
            existingRepair.setRepairType(repair.getRepairType());
            existingRepair.setShortDescription(repair.getShortDescription());
            existingRepair.setDescription(repair.getDescription());
            existingRepair.setProposedStartDate(repair.getProposedStartDate());
            existingRepair.setProposedEndDate(repair.getProposedEndDate());
            existingRepair.setProposedCost(repair.getProposedCost());
            existingRepair.setAcceptanceStatus(repair.getAcceptanceStatus());
            existingRepair.setRepairStatus(repair.getRepairStatus());
            existingRepair.setActualStartDate(repair.getActualStartDate());
            existingRepair.setActualEndDate(repair.getActualEndDate());

            Optional<Repair> updatedRepair = repairRepository.save(existingRepair);
            return updatedRepair;
        } catch (CustomException e) {
            log.error("Error updating repair: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves a Repair by its ID.
     *
     * @param id the ID of the repair
     * @return an Optional containing the Repair if found, or an empty Optional
     * if not
     */
    @Override
    public Optional<Repair> getRepairById(Long id) {
        return repairRepository.getById(id);
    }

    /**
     * Retrieves all Repairs from the repository.
     *
     * @return a List of all Repairs
     */
    @Override
    public List<Repair> getAllRepairs() {
        return repairRepository.getAll();
    }

    /**
     * Retrieves all pending Repairs.
     *
     * @return a List of pending Repairs
     */
    @Override
    public List<Repair> getPendingRepairs() {
        return repairRepository.findPendingRepairs();
    }

    /**
     * Retrieves all pending Repairs for a specific User.
     *
     * @param user the User whose pending Repairs are to be retrieved
     * @return a List of pending Repairs associated with the specified User
     */
    @Override
    public List<Repair> getPendingRepairsByUser(User user) {
        return repairRepository.findPendingRepairsByUser(user);
    }

    /**
     * Retrieves all Repairs that are currently in progress.
     *
     * @return a List of Repairs that are in progress
     */
    @Override
    public List<Repair> getInProgressRepairs() {
        return repairRepository.findInProgressRepairs();
    }

    /**
     * Retrieves all Repairs associated with a specific User ID.
     *
     * @param userId the ID of the User whose Repairs are to be retrieved
     * @return a List of Repairs associated with the specified User ID
     */
    @Override
    public List<Repair> getRepairsByUserId(Long userId) {
        return repairRepository.findRepairsByUserId(userId);
    }

    /**
     * Retrieves all Repairs associated with a specific Property.
     *
     * @param property the Property whose Repairs are to be retrieved
     * @return a List of Repairs associated with the specified Property
     */
    @Override
    public List<Repair> getRepairsByProperty(Property property) {
        return repairRepository.findRepairsByPropertyId(property);
    }

    /**
     * Retrieves all accepted Repairs.
     *
     * @return a List of accepted Repairs
     */
    @Override
    public List<Repair> getAcceptedRepairs() {
        return repairRepository.findAcceptedRepairs();
    }

    /**
     * Retrieves all Repairs that are in progress today.
     *
     * @return a List of Repairs that are in progress today
     */
    @Override
    public List<Repair> getInprogressRepairsToday() {
        return repairRepository.findInprogressRepairsToday();
    }

    /**
     * Retrieves Repairs that were made on a specific date for a User.
     *
     * @param date the date to filter Repairs
     * @param userId the ID of the User whose Repairs are to be retrieved
     * @return a List of Repairs made on the specified date for the User
     */
    @Override
    public List<Repair> getRepairsByDate(String date, Long userId) {
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startDate = parsedDate.atStartOfDay();
        LocalDateTime endDate = parsedDate.atTime(23, 59, 59);

        return repairRepository.findRepairsByDates(startDate, endDate, userId);
    }

    /**
     * Retrieves Repairs made within a specified date range for a User.
     *
     * @param startDateStr the start date of the range
     * @param endDateStr the end date of the range
     * @param userId the ID of the User whose Repairs are to be retrieved
     * @return a List of Repairs made within the specified date range for the
     * User
     */
    @Override
    public List<Repair> getRepairsByRangeOfDates(String startDateStr, String endDateStr, Long userId) {
        LocalDateTime startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);

        return repairRepository.findRepairsByDates(startDate, endDate, userId);
    }

    /**
     * Permanently deletes a Repair by its ID.
     *
     * @param id the ID of the repair to be deleted
     * @return true if the Repair was successfully deleted, false if not found
     */
    @Override
    @Transactional
    public boolean deleteRepairPermantly(Long id) {
        return repairRepository.deleteById(id);
    }

    /**
     * Safely deletes a Repair by marking it as deleted.
     *
     * @param id the ID of the repair to be deleted
     * @return true if the Repair was successfully marked as deleted, false if
     * not found
     */
    @Override
    @Transactional
    public boolean deleteRepairSafely(Long id) {
        Optional<Repair> repair = repairRepository.getById(id);
        if (repair.isPresent()) {
            return repairRepository.softDelete(repair.get());
        } else {
            log.warn("Repair with ID: {} not found for soft delete", id);
            return false;
        }
    }
}
