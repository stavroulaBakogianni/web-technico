package gr.europeandynamics.web.technico.repositories;

import gr.europeandynamics.web.technico.models.Property;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@NoArgsConstructor
public class PropertyRepositoryImpl implements Repository<Property, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves a Property entity to the database.
     *
     * @param property the Property entity to save
     * @return an Optional containing the saved Property if successful, or an
     * empty Optional if an error occurs
     */
    @Override
    @Transactional
    public Optional<Property> save(Property property) {
        try {
            entityManager.persist(property);
            return Optional.of(property);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }

    }

    /**
     * Retrieves a Property entity by its unique ID.
     *
     * @param id the ID of the Property to retrieve
     * @return an Optional containing the Property if found, or an empty
     * Optional if not
     */
    @Override
    public Optional<Property> getById(Long id) {
        Property property;
        try {
            property = entityManager.find(getEntityClass(), id);
            return Optional.of(property);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves all Property entities from the database.
     *
     * @return a List of all Properties
     */
    @Override
    public List<Property> getAll() {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Deletes a Property entity by its unique ID.
     *
     * @param id the ID of the Property to delete
     * @return true if the Property was successfully deleted, false otherwise
     */
    @Transactional
    @Override
    public boolean deleteById(Long id) {
        try {
            Property property = entityManager.find(getEntityClass(), id);
            if (property != null) {
                entityManager.remove(property);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a Property entity by its E9 identifier.
     *
     * @param e9 the E9 identifier of the Property to retrieve
     * @return an Optional containing the Property if found, or an empty
     * Optional if not
     */
    public Optional<Property> findPropertyByE9(String e9) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where e9 = :e9 ",
                        getEntityClass())
                        .setParameter("e9", e9);
        return query.getResultStream().findFirst();
    }

    /**
     * Retrieves a list of Property entities associated with a specific user's
     * VAT number.
     *
     * @param vat the VAT number of the User whose Properties to retrieve
     * @return a List of Properties associated with the given VAT
     */
    public List<Property> findPropertiesByVAT(String vat) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where user.vat = :vat ",
                        getEntityClass())
                        .setParameter("vat", vat);
        return query.getResultList();
    }

    /**
     * Gets the entity class for Property.
     *
     * @return the Property class
     */
    private Class<Property> getEntityClass() {
        return Property.class;
    }

    /**
     * Gets the entity class name for Property.
     *
     * @return the fully qualified name of the Property class
     */
    private String getEntityClassName() {
        return Property.class.getName();
    }
}
