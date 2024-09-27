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

    @Override
    public Optional<Property> findPropertyByE9(String e9) {
        return propertyRepository.findPropertyByE9(e9);
    }

    @Override
    public List<Property> findPropertyByVAT(String vat) {
        return propertyRepository.findPropertiesByVAT(vat);
    }

    @Override
    public List<Property> findAllProperties() {
        return propertyRepository.getAll();
    }

    @Override
    public Optional<Property> findPropertyByID(Long id) {
        return propertyRepository.getById(id);
    }

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
