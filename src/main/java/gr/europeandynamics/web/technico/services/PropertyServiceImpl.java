package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.exceptions.CustomException;
import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.PropertyType;
import gr.europeandynamics.web.technico.repositories.PropertyRepositoryImpl;
import gr.europeandynamics.web.technico.repositories.UserRepositoryImpl;
import gr.europeandynamics.web.technico.validators.PropertyValidator;
import gr.europeandynamics.web.technico.models.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class PropertyServiceImpl implements PropertyService {

    @Inject
    private PropertyRepositoryImpl propertyRepository;

    @Inject
    private UserRepositoryImpl userRepository;

    @Inject
    private PropertyValidator propertyValidator;

    /**
     * Creates a new Property with the provided details.
     *
     * @param e9 the E9 number of the property
     * @param address the address of the property
     * @param year the construction year of the property
     * @param propertyType the type of the property
     * @param vat the VAT number of the user who owns the property
     * @return an Optional containing the created Property if successful, or an
     * empty Optional if an error occurs
     */
    @Override
    @Transactional
    public Optional<Property> createProperty(String e9, String address, int year, PropertyType propertyType, String vat) {
        try {
            propertyValidator.validateE9(e9);
            propertyValidator.validatePropertyAddress(address);
            propertyValidator.validateConstructionYear(year);
            propertyValidator.validatePropertyType(propertyType);

            Optional<User> userOptional = userRepository.getUserByVat(vat);
            if (userOptional.isEmpty()) {
                log.error("User with VAT {} not found.", vat);
                throw new CustomException("User with VAT " + vat + " not found.");
            }
            Property property = new Property();
            property.setE9(e9);
            property.setPropertyAddress(address);
            property.setConstructionYear(year);
            property.setPropertyType(propertyType);
            property.setUser(userOptional.get());
            Optional<Property> savedProperty = propertyRepository.save(property);
            return savedProperty;
        } catch (CustomException e) {
            log.error("Error creating property: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Updates the details of an existing Property.
     *
     * @param property the Property entity with updated details
     * @return an Optional containing the updated Property if successful, or an
     * empty Optional if the property does not exist
     */
    @Override
    @Transactional
    public Optional<Property> updateProperty(Property property) {
        try {
            propertyValidator.validateProperty(property);
            Optional<Property> existingPropertyOptional = propertyRepository.getById(property.getId());
            if (existingPropertyOptional.isEmpty()) {
                log.error("Property with ID {} not found.", property.getId());
                throw new CustomException("Property with ID " + property.getId() + " not found.");
            }
            Property existingProperty = existingPropertyOptional.get();
            existingProperty.setE9(property.getE9());
            existingProperty.setPropertyAddress(property.getPropertyAddress());
            existingProperty.setConstructionYear(property.getConstructionYear());
            existingProperty.setPropertyType(property.getPropertyType());
            existingProperty.setUser(property.getUser());
            Optional<Property> updatedProperty = propertyRepository.save(existingProperty);
            return updatedProperty;
        } catch (CustomException e) {
            log.error("Error updating property: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Finds a Property by its E9 number.
     *
     * @param e9 the E9 number of the property
     * @return an Optional containing the Property if found, or an empty
     * Optional if not
     */
    @Override
    public Optional<Property> findPropertyByE9(String e9) {
        return propertyRepository.findPropertyByE9(e9);
    }

    /**
     * Finds all Properties associated with a given VAT number.
     *
     * @param vat the VAT number of the user whose properties are to be
     * retrieved
     * @return a List of Properties associated with the specified VAT number
     */
    @Override
    public List<Property> findPropertyByVAT(String vat) {
        return propertyRepository.findPropertiesByVAT(vat);
    }

    /**
     * Retrieves all Properties from the repository.
     *
     * @return a List of all Properties
     */
    @Override
    public List<Property> findAllProperties() {
        return propertyRepository.getAll();
    }

    /**
     * Finds a Property by its ID.
     *
     * @param id the ID of the property
     * @return an Optional containing the Property if found, or an empty
     * Optional if not
     */
    @Override
    public Optional<Property> findPropertyByID(Long id) {
        return propertyRepository.getById(id);
    }

    /**
     * Safely deletes a Property by marking it as deleted.
     *
     * @param id the ID of the property to be deleted
     * @return true if the Property was successfully marked as deleted, false if
     * not found
     */
    @Override
    @Transactional
    public boolean deletePropertyByIdSafely(Long id) {
        Optional<Property> propertyOptional = propertyRepository.getById(id);
        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            property.setDeleted(true);
            propertyRepository.save(property);
            return true;
        } else {
            log.warn("Property with ID: {} not found for soft delete.", id);
            return false;
        }
    }

    /**
     * Permanently deletes a Property by its ID.
     *
     * @param id the ID of the property to be deleted
     * @return true if the Property was successfully deleted, false if not found
     */
    @Override
    @Transactional
    public boolean deletePropertyByIdPermenantly(Long id) {
        boolean isDeleted = propertyRepository.deleteById(id);
        if (!isDeleted) {
            log.warn("Property with ID: {} not found for permanent delete.", id);
        }
        return isDeleted;
    }
}
