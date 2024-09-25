package gr.europeandynamics.web.technico.service;

import gr.europeandynamics.web.technico.model.Property;
import gr.europeandynamics.web.technico.model.Repair;
import gr.europeandynamics.web.technico.model.RepairType;
import gr.technico.technikon.model.User;
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

    List<Repair> getRepairsByUser(User user);

    List<Repair> getRepairsByProperty(Property property);

    List<Repair> getAcceptedRepairs();

    List<Repair> getRepairsByDate(String date, User user);

    List<Repair> getRepairsByRangeOfDates(String startDate, String endDate, User user);

    boolean deleteRepairPermantly(Long id);

    boolean deleteRepairSafely(Long id);

}
