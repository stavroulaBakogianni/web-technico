package gr.europeandynamics.web.technico.resources;

import gr.europeandynamics.web.technico.models.User;
import gr.europeandynamics.web.technico.services.UserServiceImpl;
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

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserServiceImpl userService;

    /**
     * Creates a new user.
     *
     * @param user the User object containing the information of the user to be
     * created
     * @return a Response indicating the outcome of the user creation
     */
    @POST
    @Path("create")
    public Response createUser(User user) {
        Optional<User> createdUser = userService.createUser(
                user.getVat(),
                user.getName(),
                user.getSurname(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword()
        );
        if (createdUser.isPresent()) {
            return Response.status(Response.Status.CREATED).entity(createdUser.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("User creation failed").build();
        }
    }

    /**
     * Retrieves all users.
     *
     * @return a Response containing a list of all users
     */
    @GET
    @Path("/staffMember/allUsers")
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    /**
     * Retrieves a user by their VAT number.
     *
     * @param vat the VAT number of the user to retrieve
     * @return a Response containing the user if found, or a not found message
     */
    @GET
    @Path("/staffMember/byVat/{vat}")
    public Response getUserByVat(@PathParam("vat") String vat) {
        Optional<User> user = userService.getUserByVat(vat);
        if (user.isPresent()) {
            return Response.status(Response.Status.FOUND).entity(user.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return a Response containing the user if found, or a not found message
     */
    @GET
    @Path("/staffMember/byEmail/{email}")
    public Response getUserByEmail(@PathParam("email") String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    /**
     * Retrieves a user by their email and password.
     * 
     * This is not a secure request and is for demo purposes only.
     * 
     *
     * @param email the email address of the user
     * @param password the password of the user
     * @return a Response containing the user if valid credentials are provided,
     * or an unauthorized message
     */
    @GET
    @Path("{email}/{password}")
    public Response getUserByEmailAndPassword(@PathParam("email") String email,
            @PathParam("password") String password) {
        Optional<User> user = userService.getUserByEmailAndPassword(email, password);
        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid email or password").build();
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id the ID of the user to update
     * @param user the User object containing the updated user information
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, User user) {
        if (!id.equals(user.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID in path and request body must match").build();
        }
        Optional<User> updatedUser = userService.updateUser(user);
        if (updatedUser.isPresent()) {
            return Response.ok(updatedUser.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found or could not be updated").build();
        }
    }

    /**
     * Deletes a user safely by their VAT number.
     *
     * @param vat the VAT number of the user to delete
     * @return a Response indicating the outcome of the safe deletion operation
     */
    @DELETE
    @Path("/safe/{vat}")
    public Response deleteUserSafely(@PathParam("vat") String vat) {
        boolean isDeletedSafely = userService.deleteUserSafely(vat);
        if (isDeletedSafely) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found or could not be safely deleted").build();
        }
    }

    /**
     * Permanently deletes a user by their VAT number.
     *
     * @param vat the VAT number of the user to delete
     * @return a Response indicating the outcome of the permanent deletion
     * operation
     */
    @DELETE
    @Path("staffMember/permanent/{vat}")
    public Response deleteUserPermanently(@PathParam("vat") String vat) {
        boolean isDeleted = userService.deleteUserPermanently(vat);
        if (isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found or could not be deleted").build();
        }
    }
}
