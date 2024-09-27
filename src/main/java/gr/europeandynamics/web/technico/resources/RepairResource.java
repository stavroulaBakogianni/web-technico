package gr.europeandynamics.web.technico.resources;

import gr.europeandynamics.web.technico.models.Repair;
import gr.europeandynamics.web.technico.services.RepairService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/repairs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RepairResource {

    @Inject
    private RepairService repairService;

    /**
     * Creates a new repair.
     *
     * @param repair the Repair object containing the details of the repair to be created
     * @return a Response indicating the outcome of the repair creation
     */
    @POST
    @Path("/createRepair")
    public Response createRepair(Repair repair) {
        Optional<Repair> createdRepair = repairService.createRepair(repair.getRepairType(), repair.getShortDescription(), repair.getDescription(), repair.getProperty());
        if (createdRepair.isPresent()) {
            return Response.status(Response.Status.CREATED).entity(createdRepair.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Repair could not be created").build();
        }
    }

    /**
     * Updates an existing repair.
     *
     * @param id the ID of the repair to update
     * @param repair the Repair object containing the updated details
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateRepair(@PathParam("id") Long id, Repair repair) {
        Optional<Repair> existingRepair = repairService.getRepairById(id);
        if (existingRepair.isPresent()) {
            repair.setId(id);
            Optional<Repair> updatedRepair = repairService.updateRepair(repair);
            return Response.ok(updatedRepair.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Repair not found").build();
        }
    }

    /**
     * Retrieves all repairs.
     *
     * @return a Response containing a list of all repairs
     */
    @GET
    @Path("staffMember/allRepairs")
    public Response getAllRepairs() {
        List<Repair> repairs = repairService.getAllRepairs();
        return Response.ok(repairs).build();
    }

    /**
     * Retrieves a repair by its ID.
     *
     * @param id the ID of the repair to retrieve
     * @return a Response containing the repair if found, or a not found message
     */
    @GET
    @Path("/{id}")
    public Response getRepairById(@PathParam("id") Long id) {
        Optional<Repair> repair = repairService.getRepairById(id);
        if (repair.isPresent()) {
            return Response.ok(repair.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Repair not found").build();
        }
    }

    /**
     * Safely deletes a repair by its ID.
     *
     * @param id the ID of the repair to delete
     * @return a Response indicating the outcome of the safe deletion operation
     */
    @DELETE
    @Path("/safe/{id}")
    public Response deleteRepairSafely(@PathParam("id") Long id) {
        boolean deleted = repairService.deleteRepairSafely(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Repair not found").build();
        }
    }

    /**
     * Permanently deletes a repair by its ID.
     *
     * @param id the ID of the repair to delete
     * @return a Response indicating the outcome of the permanent deletion operation
     */
    @DELETE
    @Path("staffMember/permanent/{id}")
    public Response deleteRepairPermanently(@PathParam("id") Long id) {
        boolean deleted = repairService.deleteRepairPermantly(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Repair not found").build();
        }
    }

    /**
     * Retrieves all pending repairs.
     *
     * @return a Response containing a list of all pending repairs
     */
    @GET
    @Path("staffMember/pendingRepairs")
    public Response getPendingRepairs() {
        List<Repair> pendingRepairs = repairService.getPendingRepairs();
        return Response.ok(pendingRepairs).build();
    }

    /**
     * Retrieves repairs by user ID.
     *
     * @param userId the ID of the user whose repairs to retrieve
     * @return a Response containing a list of repairs associated with the user
     */
    @GET
    @Path("byUser/{userId}")
    public Response getRepairsByUserId(@PathParam("userId") Long userId) {
        List<Repair> repairs = repairService.getRepairsByUserId(userId);
        return Response.ok(repairs).build();
    }

    /**
     * Retrieves all repairs in progress today.
     *
     * @return a Response containing a list of repairs in progress today
     */
    @GET
    @Path("staffMember/today/inprogress")
    public Response getInprogressRepairsToday() {
        List<Repair> repairs = repairService.getInprogressRepairsToday();
        return Response.ok(repairs).build();
    }

    /**
     * Retrieves repairs by date.
     *
     * @param date the date for which to retrieve repairs
     * @param userId optional user ID to filter repairs
     * @return a Response containing a list of repairs for the specified date
     */
    @GET
    @Path("/byDate/{date}")
    public Response getRepairsByDate(@PathParam("date") String date, @QueryParam("userId") Long userId) {
        List<Repair> repairs = repairService.getRepairsByDate(date, userId);
        return Response.ok(repairs).build();
    }

    /**
     * Retrieves repairs within a specified date range.
     *
     * @param startDateStr the start date of the range
     * @param endDateStr the end date of the range
     * @param userId optional user ID to filter repairs
     * @return a Response containing a list of repairs within the specified date range
     */
    @GET
    @Path("/byRange/{startDate}/{endDate}")
    public Response getRepairsByRangeOfDates(
            @PathParam("startDate") String startDateStr,
            @PathParam("endDate") String endDateStr,
            @QueryParam("userId") Long userId) {
        List<Repair> repairs = repairService.getRepairsByRangeOfDates(startDateStr, endDateStr, userId);
        return Response.ok(repairs).build();
    }
}
