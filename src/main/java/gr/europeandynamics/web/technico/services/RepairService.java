package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.Repair;
import gr.europeandynamics.web.technico.models.RepairType;
import gr.europeandynamics.web.technico.models.User;
import java.util.List;
import java.util.Optional;

public interface RepairService {

    Optional<Repair> createRepair(RepairType repairType, String shortDescription,
            String description, Property property);

    Optional<Repair> updateRepair(Repair repair);

    Optional<Repair> getRepairById(Long id);

    List<Repair> getAllRepairs();

    List<Repair> getPendingRepairs();

    List<Repair> getPendingRepairsByUser(User user);

    List<Repair> getInProgressRepairs();

    List<Repair> getInprogressRepairsToday();

    List<Repair> getRepairsByUserId(Long userId);

    List<Repair> getRepairsByProperty(Property property);

    List<Repair> getAcceptedRepairs();

    List<Repair> getRepairsByDate(String date, Long userId);

    List<Repair> getRepairsByRangeOfDates(String startDate, String endDate, Long userId);

    boolean deleteRepairPermantly(Long id);

    boolean deleteRepairSafely(Long id);

}
