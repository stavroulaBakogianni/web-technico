package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Role;
import gr.europeandynamics.web.technico.repositories.UserRepositoryImpl;
import gr.europeandynamics.web.technico.validators.UserValidator;
import gr.europeandynamics.web.technico.models.User;
import jakarta.enterprise.context.RequestScoped;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import java.util.List;

@Slf4j
@RequestScoped
public class UserServiceImpl implements UserService {

    @Inject
    private UserRepositoryImpl userRepository;

    @Inject
    private UserValidator userValidator;

    @Override
    public Optional<User> createUser(String vat, String name, String surname, String address, String phoneNumber, String email, String password) {
        try {
            userValidator.validateVat(vat);
            userValidator.validateName(name);
            userValidator.validateSurname(surname);
            userValidator.validateAddress(address);
            userValidator.validatePhoneNumber(phoneNumber);
            userValidator.validateEmail(email);
            userValidator.validatePassword(password);

            User user = new User();
            user.setVat(vat);
            user.setName(name);
            user.setSurname(surname);
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(Role.PROPERTY_OWNER);

            return userRepository.save(user);
        } catch (CustomException e) {
            log.error("Error creating user: " + e.getMessage());
            return Optional.empty();

        }
    }

    @Override
    public Optional<User> getUserByVat(String vat) {
        try {
            // Validate VAT
            userValidator.validateVat(vat);
            return userRepository.getUserByVat(vat);
        } catch (CustomException e) {
            log.error("Error finding user by VAT: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            userValidator.validateEmail(email);
            return userRepository.getUserByEmail(email);
        } catch (CustomException e) {
            log.error("Error finding user by email: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByEmailAndPassword(String email, String password) {
        try {
            userValidator.validateEmail(email);
            userValidator.validatePassword(password);
            return userRepository.getUserByEmailAndPassword(email, password);
        } catch (CustomException e) {
            log.error("Error retrieving user by email and password: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public Role getUserRole(User user) {
        if (user != null) {
            return user.getRole();
        }
        return null;
    }

    @Transactional
    @Override
    public Optional<User> updateUser(User user) {
        try {
            Optional<User> existingUserOptional = userRepository.getById(user.getId());
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                userValidator.validateUser(user);
                existingUser.setVat(user.getVat());
                existingUser.setName(user.getName());
                existingUser.setSurname(user.getSurname());
                existingUser.setAddress(user.getAddress());
                existingUser.setPhoneNumber(user.getPhoneNumber());
                existingUser.setEmail(user.getEmail());
                existingUser.setPassword(user.getPassword());
                existingUser.setRole(Role.PROPERTY_OWNER);
                return Optional.ofNullable(userRepository.save(existingUser).orElse(null));
            } else {
                return Optional.empty();
            }
        } catch (CustomException e) {
            log.error("Error updating user: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean deleteUserPermanently(String vat) {
        try {
            userValidator.validateVat(vat);
            return userRepository.deleteUserPermanentlyByVat(vat);
        } catch (CustomException e) {
            log.error("Error deleting user permanently: " + e.getMessage());
            return false;
        }
    }

    @Transactional
    @Override
    public boolean deleteUserSafely(String vat) {
        try {
            userValidator.validateVat(vat);
            Optional<User> userOptional = userRepository.getUserByVat(vat);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setDeleted(true);
                userRepository.save(user);
                return true;
            }
            return false;
        } catch (CustomException e) {
            log.error("Error deleting user safely: " + e.getMessage());
            return false;
        }
    }
}
