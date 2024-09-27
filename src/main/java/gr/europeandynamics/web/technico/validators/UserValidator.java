package gr.europeandynamics.web.technico.validators;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Role;
import gr.europeandynamics.web.technico.models.User;
import java.util.regex.Pattern;

public class UserValidator {

    /**
     * Validates the VAT number of the user.
     *
     * @param vat the VAT number to validate
     * @throws CustomException if the VAT is null, not exactly 9 characters, or
     * contains non-numeric characters
     */
    public void validateVat(String vat) throws CustomException {
        if (vat == null || vat.length() != 9) {
            throw new CustomException("VAT must be exactly 9 characters.");
        }
        if (!vat.matches("\\d+")) {
            throw new CustomException("VAT must contain only numeric characters.");
        }
    }

    /**
     * Validates the name of the user.
     *
     * @param name the name to validate
     * @throws CustomException if the name is null, blank, or not between 1 and
     * 50 characters
     */
    public void validateName(String name) throws CustomException {
        if (name == null || name.isBlank()) {
            throw new CustomException("Name cannot be null or blank.");
        }
        if (name.length() < 1 || name.length() > 50) {
            throw new CustomException("Name must be between 1 and 50 characters.");
        }
    }

    /**
     * Validates the surname of the user.
     *
     * @param surname the surname to validate
     * @throws CustomException if the surname is null, blank, or not between 1
     * and 50 characters
     */
    public void validateSurname(String surname) throws CustomException {
        if (surname == null || surname.isBlank()) {
            throw new CustomException("Surname cannot be null or blank.");
        }
        if (surname.length() < 1 || surname.length() > 50) {
            throw new CustomException("Surname must be between 1 and 50 characters.");
        }
    }

    /**
     * Validates the address of the user.
     *
     * @param address the address to validate
     * @throws CustomException if the address exceeds 50 characters
     */
    public void validateAddress(String address) throws CustomException {
        if (address != null && address.length() > 50) {
            throw new CustomException("Address must be at most 50 characters.");
        }
    }

    /**
     * Validates the phone number of the user.
     *
     * @param phoneNumber the phone number to validate
     * @throws CustomException if the phone number exceeds 14 characters or
     * contains non-numeric characters
     */
    public void validatePhoneNumber(String phoneNumber) throws CustomException {
        if (phoneNumber != null && (phoneNumber.length() > 14 || !phoneNumber.matches("\\d+"))) {
            throw new CustomException("Phone number must contain only digits and be at most 14 characters long.");
        }
    }

    /**
     * Validates the email of the user.
     *
     * @param email the email to validate
     * @throws CustomException if the email is null, blank, or does not match a
     * valid email format
     */
    public void validateEmail(String email) throws CustomException {
        if (email == null || email.isBlank()) {
            throw new CustomException("Email cannot be null or blank.");
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new CustomException("Invalid email format.");
        }
    }

    /**
     * Validates the password of the user.
     *
     * @param password the password to validate
     * @throws CustomException if the password is null, not between 8 and 50
     * characters, or does not contain both letters and digits
     */
    public void validatePassword(String password) throws CustomException {
        if (password == null || password.length() < 8 || password.length() > 50) {
            throw new CustomException("Password must be between 8 and 50 characters.");
        }
        if (!password.matches(".*\\d.*") || !password.matches(".*[A-Za-z].*")) {
            throw new CustomException("Password must contain at least one letter and one digit.");
        }
    }

    /**
     * Validates the role of the user.
     *
     * @param role the role to validate
     * @throws CustomException if the role is null
     */
    public void validateRole(Role role) throws CustomException {
        if (role == null) {
            throw new CustomException("Role cannot be null.");
        }
    }

    /**
     * Validates all properties of a given User object.
     *
     * @param user the User object to validate
     * @throws CustomException if any property validation fails
     */
    public void validateUser(User user) throws CustomException {
        validateVat(user.getVat());
        validateName(user.getName());
        validateSurname(user.getSurname());
        validateAddress(user.getAddress());
        validatePhoneNumber(user.getPhoneNumber());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateRole(user.getRole());
    }
}
