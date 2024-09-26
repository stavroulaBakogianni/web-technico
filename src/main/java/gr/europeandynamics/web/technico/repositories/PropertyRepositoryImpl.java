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

    @Override
    public List<Property> getAll() {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

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

    public Optional<Property> findPropertyByE9(String e9) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where e9 = :e9 ",
                        getEntityClass())
                        .setParameter("e9", e9);
        return query.getResultStream().findFirst();
    }

    public List<Property> findPropertiesByVAT(String vat) {
        TypedQuery<Property> query
                = entityManager.createQuery("from " + getEntityClassName()
                        + " where user_vat = :vat ",
                        getEntityClass())
                        .setParameter("vat", vat);
        return query.getResultList();
    }

    private Class<Property> getEntityClass() {
        return Property.class;
    }

    private String getEntityClassName() {
        return Property.class.getName();
    }
}
