package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.models.Role;
import gr.europeandynamics.web.technico.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> createUser(String vat, String name, String surname, String address, String phoneNumber, String email, String password);

    Optional<User> getUserByVat(String vat);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByEmailAndPassword(String email, String password);

    List<User> getAllUsers();

    Role getUserRole(User user);

    Optional<User> updateUser(User user);

    boolean deleteUserPermanently(String vat);

    boolean deleteUserSafely(String vat);

}
