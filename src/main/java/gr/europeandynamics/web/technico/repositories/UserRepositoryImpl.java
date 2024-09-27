package gr.europeandynamics.web.technico.repositories;

import gr.europeandynamics.web.technico.models.User;
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
public class UserRepositoryImpl implements Repository<User, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves a User entity to the database.
     *
     * @param user the User entity to save
     * @return an Optional containing the saved User if successful, or an empty
     * Optional if an error occurs
     */
    @Transactional
    @Override
    public Optional<User> save(User user) {
        try {
            entityManager.persist(user);
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves a User entity by its VAT number.
     *
     * @param vat the VAT number of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional if
     * not
     */
    public Optional<User> getUserByVat(String vat) {

        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE vat = :vat", User.class);
        query.setParameter("vat", vat);
        return query.getResultStream().findFirst();
    }

    /**
     * Retrieves a User entity by its email address.
     *
     * @param email the email address of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional if
     * not
     */
    public Optional<User> getUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    /**
     * Retrieves a User entity by its email address and password. Only
     * non-deleted users are considered.
     *
     * @param email the email address of the User to retrieve
     * @param password the password of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional if
     * not
     */
    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        TypedQuery<User> query = entityManager.createQuery(
                "FROM User WHERE email = :email AND password = :password AND isDeleted = false",
                User.class
        );
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.getResultStream().findFirst();
    }

    /**
     * Permanently deletes a User entity by its VAT number.
     *
     * @param vat the VAT number of the User to delete
     * @return true if the User was successfully deleted, false otherwise
     */
    @Transactional
    public boolean deleteUserPermanentlyByVat(String vat) {
        try {

            Optional<User> optionalUser = getUserByVat(vat);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                entityManager.remove(user);
                return true;
            }
            return false;

        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a User entity by its unique ID.
     *
     * @param id the ID of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional if
     * not
     */
    @Override
    public Optional<User> getById(Long id) {
        User user;
        try {
            user = entityManager.find(getEntityClass(), id);
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();

        }
    }

    /**
     * Retrieves all User entities from the database.
     *
     * @return a List of all Users
     */
    @Override
    public List<User> getAll() {
        TypedQuery<User> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

    /**
     * Deletes a User entity by its unique ID.
     *
     * @param id the ID of the User to delete
     * @return true if the User was successfully deleted, false otherwise
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        try {
            User persistentInstance = entityManager.find(getEntityClass(), id);

            if (persistentInstance != null) {
                entityManager.remove(persistentInstance);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Gets the entity class for User.
     *
     * @return the User class
     */
    private Class<User> getEntityClass() {
        return User.class;
    }

    /**
     * Gets the entity class name for User.
     *
     * @return the fully qualified name of the User class
     */
    private String getEntityClassName() {
        return User.class.getName();
    }
}
