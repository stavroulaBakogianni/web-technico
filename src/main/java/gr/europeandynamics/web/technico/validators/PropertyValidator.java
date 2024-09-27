package gr.europeandynamics.web.technico.validators;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Property;

public class PropertyValidator {

    /**
     * Validates the E9 identifier for a property.
     *
     * @param e9 the E9 identifier to validate
     * @throws CustomException if the E9 does not contain exactly 20 characters
     */
    public void validateE9(String e9) throws CustomException {
        if (e9 == null || e9.length() != 20) {
            throw new CustomException("E9 must contain exactly 20 characters.");
        }
    }

    /**
     * Validates the address of the property.
     *
     * @param address the address to validate
     * @throws CustomException if the address exceeds 50 characters
     */
    public void validatePropertyAddress(String address) throws CustomException {
        if (address != null && address.length() > 50) {
            throw new CustomException("Property address must be at most 50 characters.");
        }
    }

    /**
     * Validates the construction year of the property.
     *
     * @param year the construction year to validate
     * @throws CustomException if the year is not a valid 4-digit year
     */
    public void validateConstructionYear(int year) throws CustomException {
        if (year < 1000 || year > 9999) {
            throw new CustomException("Construction year must be a valid 4-digit year.");
        }
    }

    /**
     * Validates the type of the property.
     *
     * @param propertyType the property type to validate
     * @throws CustomException if the property type is null
     */
    public void validatePropertyType(Enum<?> propertyType) throws CustomException {
        if (propertyType == null) {
            throw new CustomException("Property type cannot be null.");
        }
    }

    /**
     * Validates all properties of a given Property object.
     *
     * @param property the Property object to validate
     * @throws CustomException if any property validation fails
     */
    public void validateProperty(Property property) throws CustomException {
        validateE9(property.getE9());
        validatePropertyAddress(property.getPropertyAddress());
        validateConstructionYear(property.getConstructionYear());
        validatePropertyType(property.getPropertyType());
    }
}
