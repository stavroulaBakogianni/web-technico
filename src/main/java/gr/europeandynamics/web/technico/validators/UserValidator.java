package gr.europeandynamics.web.technico.validators;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Role;
import gr.europeandynamics.web.technico.models.User;
import java.util.regex.Pattern;

public class UserValidator {

    public void validateVat(String vat) throws CustomException {
        if (vat == null || vat.length() != 9) {
            throw new CustomException("VAT must be exactly 9 characters.");
        }
        if (!vat.matches("\\d+")) {
            throw new CustomException("VAT must contain only numeric characters.");
        }
    }

    public void validateName(String name) throws CustomException {
        if (name == null || name.isBlank()) {
            throw new CustomException("Name cannot be null or blank.");
        }
        if (name.length() < 1 || name.length() > 50) {
            throw new CustomException("Name must be between 1 and 50 characters.");
        }
    }

    public void validateSurname(String surname) throws CustomException {
        if (surname == null || surname.isBlank()) {
            throw new CustomException("Surname cannot be null or blank.");
        }
        if (surname.length() < 1 || surname.length() > 50) {
            throw new CustomException("Surname must be between 1 and 50 characters.");
        }
    }

    public void validateAddress(String address) throws CustomException {
        if (address != null && address.length() > 50) {
            throw new CustomException("Address must be at most 50 characters.");
        }
    }

    public void validatePhoneNumber(String phoneNumber) throws CustomException {
        if (phoneNumber != null && (phoneNumber.length() > 14 || !phoneNumber.matches("\\d+"))) {
            throw new CustomException("Phone number must contain only digits and be at most 14 characters long.");
        }
    }

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

    public void validatePassword(String password) throws CustomException {
        if (password == null || password.length() < 8 || password.length() > 50) {
            throw new CustomException("Password must be between 8 and 50 characters.");
        }
        if (!password.matches(".*\\d.*") || !password.matches(".*[A-Za-z].*")) {
            throw new CustomException("Password must contain at least one letter and one digit.");
        }
    }

    public void validateRole(Role role) throws CustomException {
        if (role == null) {
            throw new CustomException("Role cannot be null.");
        }
    }

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
