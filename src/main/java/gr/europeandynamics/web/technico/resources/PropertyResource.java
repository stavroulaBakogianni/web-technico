package gr.europeandynamics.web.technico.resources;

import gr.europeandynamics.web.technico.models.Property;
import gr.europeandynamics.web.technico.services.PropertyService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/properties")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PropertyResource {

    @Inject
    private PropertyService propertyService;

    /**
     * Creates a new property.
     *
     * @param property the Property object containing the details of the
     * property to be created
     * @return a Response indicating the outcome of the property creation
     */
    @POST
    @Path("/createProperty")
    public Response createProperty(Property property) {
        Optional<Property> createdProperty = propertyService.createProperty(
                property.getE9(),
                property.getPropertyAddress(),
                property.getConstructionYear(),
                property.getPropertyType(),
                property.getUser().getVat()
        );
        if (createdProperty.isPresent()) {
            return Response.status(Response.Status.CREATED).entity(createdProperty.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Property creation failed").build();
        }
    }

    /**
     * Updates an existing property.
     *
     * @param id the ID of the property to update
     * @param property the Property object containing the updated details
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateProperty(@PathParam("id") Long id, Property property) {
        property.setId(id);
        Optional<Property> updatedProperty = propertyService.updateProperty(property);
        if (updatedProperty.isPresent()) {
            return Response.ok(updatedProperty.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Property not found").build();
        }
    }

    /**
     * Retrieves a property by its E9 identifier.
     *
     * @param e9 the E9 identifier of the property to retrieve
     * @return a Response containing the property if found, or a not found
     * message
     */
    @GET
    @Path("/byE9/{e9}")
    public Response getPropertyByE9(@PathParam("e9") String e9) {
        Optional<Property> property = propertyService.findPropertyByE9(e9);
        if (property.isPresent()) {
            return Response.ok(property.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Property not found").build();
        }
    }

    /**
     * Retrieves properties associated with a specific VAT number.
     *
     * @param vat the VAT number for which to retrieve properties
     * @return a Response containing a list of properties associated with the
     * VAT
     */
    @GET
    @Path("/byVat/{vat}")
    public Response getPropertiesByVat(@PathParam("vat") String vat) {
        List<Property> properties = propertyService.findPropertyByVAT(vat);
        if (!properties.isEmpty()) {
            return Response.ok(properties).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No properties found for VAT: " + vat).build();
        }
    }

    /**
     * Retrieves a property by its ID.
     *
     * @param id the ID of the property to retrieve
     * @return a Response containing the property if found, or a not found
     * message
     */
    @GET
    @Path("/{id}")
    public Response getPropertyById(@PathParam("id") Long id) {
        Optional<Property> property = propertyService.findPropertyByID(id);
        if (property.isPresent()) {
            return Response.ok(property.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Property not found").build();
        }
    }

    /**
     * Retrieves all properties.
     *
     * @return a Response containing a list of all properties
     */
    @GET
    @Path("/staffMember/allProperties")
    public Response getAllProperties() {
        List<Property> properties = propertyService.findAllProperties();
        return Response.ok(properties).build();
    }

    /**
     * Safely deletes a property by its ID.
     *
     * @param id the ID of the property to delete
     * @return a Response indicating the outcome of the safe deletion operation
     */
    @DELETE
    @Path("/safe/{id}")
    public Response deletePropertySafely(@PathParam("id") Long id) {
        boolean isDeleted = propertyService.deletePropertyByIdSafely(id);
        if (isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Property not found").build();
        }
    }

    /**
     * Permanently deletes a property by its ID.
     *
     * @param id the ID of the property to delete
     * @return a Response indicating the outcome of the permanent deletion
     * operation
     */
    @DELETE
    @Path("/staffMember/permanent/{id}")
    public Response deletePropertyPermanently(@PathParam("id") Long id) {
        boolean isDeleted = propertyService.deletePropertyByIdPermenantly(id);
        if (isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Property not found").build();
        }
    }
}
