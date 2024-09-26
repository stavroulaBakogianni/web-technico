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

    public Optional<User> getUserByVat(String vat) {

        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE vat = :vat", User.class);
        query.setParameter("vat", vat);
        return query.getResultStream().findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        TypedQuery<User> query = entityManager.createQuery(
                "FROM User WHERE email = :email AND password = :password AND isDeleted = false",
                User.class
        );
        query.setParameter("email", email);
        query.setParameter("password", password);
        return query.getResultStream().findFirst();
    }

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

    @Override
    public List<User> getAll() {
        TypedQuery<User> query
                = entityManager.createQuery("from " + getEntityClassName(), getEntityClass());
        return query.getResultList();
    }

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

    private Class<User> getEntityClass() {
        return User.class;
    }

    private String getEntityClassName() {
        return User.class.getName();
    }
}
