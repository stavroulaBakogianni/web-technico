package gr.europeandynamics.web.technico.service;

import gr.europeandynamics.web.technico.model.Role;
import gr.technico.technikon.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> createUser(String vat, String name, String surname, String address, String phoneNumber, String email, String password);

    Optional<User> getUserByVat(String vat);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    Role getUserRole(User user);

    Optional<User> updateUser(User user);

    boolean deleteUserPermanently(String vat);

    boolean deleteUserSafely(String vat);

}
