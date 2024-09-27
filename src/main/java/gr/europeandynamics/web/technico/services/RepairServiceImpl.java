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

    @Override
    public Optional<Repair> getRepairById(Long id) {
        return repairRepository.getById(id);
    }

    @Override
    public List<Repair> getAllRepairs() {
        return repairRepository.getAll();
    }

    @Override
    public List<Repair> getPendingRepairs() {
        return repairRepository.findPendingRepairs();
    }

    @Override
    public List<Repair> getPendingRepairsByUser(User user) {
        return repairRepository.findPendingRepairsByUser(user);
    }

    @Override
    public List<Repair> getInProgressRepairs() {
        return repairRepository.findInProgressRepairs();
    }

    @Override
    public List<Repair> getRepairsByUserId(Long userId) {
        return repairRepository.findRepairsByUserId(userId);
    }

    @Override
    public List<Repair> getRepairsByProperty(Property property) {
        return repairRepository.findRepairsByPropertyId(property);
    }

    @Override
    public List<Repair> getAcceptedRepairs() {
        return repairRepository.findAcceptedRepairs();
    }

    @Override
    public List<Repair> getInprogressRepairsToday() {
        return repairRepository.findInprogressRepairsToday();
    }

    @Override
    public List<Repair> getRepairsByDate(String date, Long userId) {
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startDate = parsedDate.atStartOfDay();
        LocalDateTime endDate = parsedDate.atTime(23, 59, 59);

        return repairRepository.findRepairsByDates(startDate, endDate, userId);
    }

    @Override
    public List<Repair> getRepairsByRangeOfDates(String startDateStr, String endDateStr, Long userId) {
        LocalDateTime startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);

        return repairRepository.findRepairsByDates(startDate, endDate, userId);
    }

    @Override
    @Transactional
    public boolean deleteRepairPermantly(Long id) {
        return repairRepository.deleteById(id);
    }

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
