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

    @Override
    public List<Repair> getAll() {
        TypedQuery<Repair> query = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

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

    @Transactional
    public boolean softDelete(Repair repair) {
        repair.setDeleted(true);
        return save(repair).isPresent();
    }

    public List<Repair> findPendingRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repairStatus = :repairStatus ",
                        getEntityClass())
                        .setParameter("repairStatus", RepairStatus.PENDING);
        return query.getResultList();
    }

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

    public List<Repair> findRepairsByPropertyId(Property property) {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where property = :property ",
                        getEntityClass())
                        .setParameter("property", property);
        return query.getResultList();
    }

    public List<Repair> findInProgressRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where repairStatus = :repairStatus ",
                        getEntityClass())
                        .setParameter("repairStatus", RepairStatus.INPROGRESS);
        return query.getResultList();
    }

    public List<Repair> findAcceptedRepairs() {
        TypedQuery<Repair> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where acceptance_status = 1 ",
                        getEntityClass());
        return query.getResultList();
    }

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

    private Class<Repair> getEntityClass() {
        return Repair.class;
    }

    private String getEntityClassName() {
        return Repair.class.getName();
    }
}
