package gr.europeandynamics.web.technico.validator;

import gr.europeandynamics.web.technico.exception.CustomException;
import gr.europeandynamics.web.technico.model.Property;

public class PropertyValidator {

    public void validateE9(String e9) throws CustomException {
        if (e9 == null || e9.length() != 20) {
            throw new CustomException("E9 must contain exactly 20 characters.");
        }
    }

    public void validatePropertyAddress(String address) throws CustomException {
        if (address != null && address.length() > 50) {
            throw new CustomException("Property address must be at most 50 characters.");
        }
    }

    public void validateConstructionYear(int year) throws CustomException {
        if (year < 1000 || year > 9999) {
            throw new CustomException("Construction year must be a valid 4-digit year.");
        }
    }

    public void validatePropertyType(Enum<?> propertyType) throws CustomException {
        if (propertyType == null) {
            throw new CustomException("Property type cannot be null.");
        }
    }

    public void validateProperty(Property property) throws CustomException {
        validateE9(property.getE9());
        validatePropertyAddress(property.getPropertyAddress());
        validateConstructionYear(property.getConstructionYear());
        validatePropertyType(property.getPropertyType());
    }
}
