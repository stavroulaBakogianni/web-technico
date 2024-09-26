package gr.europeandynamics.web.technico.services;

import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.models.PropertyType;
import java.util.List;
import java.util.Optional;

public interface PropertyService {

    Optional<Property> createProperty(String e9, String address, int year, PropertyType propertyType, String vat);

    Optional<Property> updateProperty(Property property);

    Optional<Property> findPropertyByE9(String e9);

    List<Property> findPropertyByVAT(String vat);

    List<Property> findAllProperties();

    Optional<Property> findPropertyByID(Long id);

    boolean deletePropertyByIdSafely(Long id);

    boolean deletePropertyByIdPermenantly(Long id);
}
