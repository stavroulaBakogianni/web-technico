package gr.europeandynamics.web.technico.repository;

import gr.technico.technikon.model.User;
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

    public Optional<User> findByVat(String vat) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE vat = :vat", User.class);
        query.setParameter("vat", vat);
        return query.getResultStream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        TypedQuery<User> query = entityManager.createQuery(
                "FROM User WHERE username = :username AND password = :password AND isDeleted = false",
                User.class
        );
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultStream().findFirst();
    }

    @Transactional
    public boolean deletePermanentlyByVat(String vat) {
        try {

            Optional<User> optionalUser = findByVat(vat);

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
    public Optional<User> findById(Long id) {
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
    public List<User> findAll() {
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
