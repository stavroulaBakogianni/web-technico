package gr.europeandynamics.web.technico.repositories;

import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.Repair;
import gr.europeandynamics.web.technico.models.RepairStatus;
import gr.europeandynamics.web.technico.models.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class RepairRepositoryImpl implements Repository<Repair, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves a Repair entity to the database.
     *
     * @param repair the Repair entity to save
     * @return an Optional containing the saved Repair if successful, or an
     * empty Optional if an error occurs
     */
    @Override
    @Transactional
    public Optional<Repair> save(Repair repair) {
        try {
            entityManager.persist(repair);
            return Optional.of(repair);
        } catch (Exception e) {
            log.error("Error saving repair: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves all Repair entities from the database.
     *
     * @return a List of all Repair entities
     */
    @Override
    public List<Repair> getAll() {
        TypedQuery<Repair> query = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Retrieves a Repair entity by its unique ID.
     *
     * @param id the ID of the Repair to retrieve
     * @return an Optional containing the Repair if found, or an empty Optional
     * if not
     */
    @Override
    public Optional<Repair> getById(Long id) {
        try {
            Repair repair = entityManager.find(getEntityClass(), id);
            return Optional.ofNullable(repair);
        } catch (Exception e) {
            log.error("Error finding repair by ID {}: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Deletes a Repair entity by its unique ID.
     *
     * @param id the ID of the Repair to delete
     * @return true if the Repair was successfully deleted, false otherwise
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        try {
            Repair repair = entityManager.find(getEntityClass(), id);
            if (repair != null) {
                entityManager.remove(repair);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error deleting repair by ID {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Soft deletes a Repair entity by marking it as deleted.
     *
     * @param repair the Repair entity to soft delete
     * @return true if the Repair was successfully soft deleted, false otherwise
     */
    @Transactional
    public boolean softDelete(Repair repair) {
        repair.setDeleted(true);
        return save(repair).isPresent();
    }

    /**
     * Finds all pending Repair entities.
     *
     * @return a List of pending Repairs
     */
    public List<Repair> findPendingRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repairStatus = :repairStatus ",
                        getEntityClass())
                        .setParameter("repairStatus", RepairStatus.PENDING);
        return query.getResultList();
    }

    /**
     * Finds pending Repair entities for a specific user.
     *
     * @param user the User whose pending Repairs to retrieve
     * @return a List of pending Repairs associated with the given User
     */
    public List<Repair> findPendingRepairsByUser(User user) {
        TypedQuery<Repair> query
                = entityManager.createQuery("SELECT r FROM Repair r "
                        + "JOIN r.property p "
                        + "WHERE p.user = :user "
                        + "AND r.repairStatus = :repairStatus "
                        + "AND r.proposedCost IS NOT NULL", Repair.class);
        query.setParameter("user", user);
        query.setParameter("repairStatus", RepairStatus.PENDING);

        return query.getResultList();
    }

    /**
     * Finds Repair entities by the User ID.
     *
     * @param userId the ID of the User whose Repairs to retrieve
     * @return a List of Repairs associated with the given User ID
     */
    public List<Repair> findRepairsByUserId(Long userId) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            return Collections.emptyList();
        }
        TypedQuery<Repair> query = entityManager.createQuery(
                "SELECT r FROM Repair r JOIN r.property p WHERE p.user = :user", Repair.class)
                .setParameter("user", user);
        return query.getResultList();
    }

    /**
     * Finds Repair entities by the associated Property.
     *
     * @param property the Property whose Repairs to retrieve
     * @return a List of Repairs associated with the given Property
     */
    public List<Repair> findRepairsByPropertyId(Property property) {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where property = :property ",
                        getEntityClass())
                        .setParameter("property", property);
        return query.getResultList();
    }

    /**
     * Finds all Repairs that are currently in progress.
     *
     * @return a List of Repairs that are in progress
     */
    public List<Repair> findInProgressRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repairStatus = :repairStatus ",
                        getEntityClass())
                        .setParameter("repairStatus", RepairStatus.INPROGRESS);
        return query.getResultList();
    }

    /**
     * Finds all accepted Repair entities.
     *
     * @return a List of accepted Repairs
     */
    public List<Repair> findAcceptedRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where acceptance_status = 1 ",
                        getEntityClass());
        return query.getResultList();
    }

    /**
     * Finds all Repairs that are in progress and were submitted today.
     *
     * @return a List of Repairs that are in progress and were submitted today
     */
    public List<Repair> findInprogressRepairsToday() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repairStatus = :repairStatus "
                        + "and submissionDate between :startDate and :endDate",
                        getEntityClass())
                        .setParameter("startDate", LocalDate.now().atStartOfDay())
                        .setParameter("endDate", LocalDateTime.now())
                        .setParameter("repairStatus", RepairStatus.INPROGRESS);
        return query.getResultList();
    }

    /**
     * Finds Repairs submitted between the specified dates and optionally
     * filtered by User ID.
     *
     * @param startDate the starting date for the search
     * @param endDate the ending date for the search
     * @param userId the optional User ID to filter the Repairs
     * @return a List of Repairs submitted between the specified dates
     */
    public List<Repair> findRepairsByDates(LocalDateTime startDate, LocalDateTime endDate, Long userId) {

        if (userId == null) {
            TypedQuery<Repair> query
                    = entityManager.createQuery("from " + getEntityClassName()
                            + " where submissionDate between :startDate and :endDate",
                            getEntityClass())
                            .setParameter("startDate", startDate)
                            .setParameter("endDate", endDate);

            return query.getResultList();
        } else {
            User user = entityManager.find(User.class, userId);
            if (user == null) {
                TypedQuery<Repair> query = entityManager.createQuery(
                        "from " + getEntityClassName()
                        + " where submissionDate between :startDate and :endDate",
                        getEntityClass())
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate);

                return query.getResultList();
            } else {
                TypedQuery<Repair> query
                        = entityManager.createQuery("SELECT r FROM Repair r "
                                + "JOIN r.property p "
                                + "WHERE r.submissionDate BETWEEN :startDate AND :endDate "
                                + "AND p.user = :user", Repair.class)
                                .setParameter("startDate", startDate)
                                .setParameter("endDate", endDate)
                                .setParameter("user", user);
                return query.getResultList();
            }
        }
    }

    /**
     * Gets the entity class for Repair.
     *
     * @return the Repair class
     */
    private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    /**
     * Gets the entity class name for Repair.
     *
     * @return the fully qualified name of the Repair class
     */
    private String getEntityClassName() {
        return Repair.class.getName();
    }
}
