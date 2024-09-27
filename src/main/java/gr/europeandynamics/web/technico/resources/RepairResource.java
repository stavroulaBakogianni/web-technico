package gr.europeandynamics.web.technico.resources;

import gr.europeandynamics.web.technico.models.Repair;
import gr.europeandynamics.web.technico.models.User;
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

    @POST
    @Path("propertyOwner/createRepair")
    public Response createRepair(Repair repair) {
        Optional<Repair> createdRepair = repairService.createRepair(repair.getRepairType(), repair.getShortDescription(), repair.getDescription(), repair.getProperty());
        if (createdRepair.isPresent()) {
            return Response.status(Response.Status.CREATED).entity(createdRepair.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Repair could not be created").build();
        }
    }

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

    @GET
    @Path("staffMember/allRepairs")
    public Response getAllRepairs() {
        List<Repair> repairs = repairService.getAllRepairs();
        return Response.ok(repairs).build();
    }

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

    @GET
    @Path("staffMember/pendingRepairs")
    public Response getPendingRepairs() {
        List<Repair> pendingRepairs = repairService.getPendingRepairs();
        return Response.ok(pendingRepairs).build();
    }

    @GET
    @Path("/propertyOwner/{userId}")
    public Response getRepairsByUser(@PathParam("userId") Long userId, User user) {
        List<Repair> repairs = repairService.getRepairsByUser(user);
        return Response.ok(repairs).build();
    }

    @GET
    @Path("/byDate/{date}")
    public Response getRepairsByDate(@PathParam("date") String date, User user) {
        List<Repair> repairs = repairService.getRepairsByDate(date, user);
        return Response.ok(repairs).build();
    }

    @GET
    @Path("/byRange/{startDate}/{endDate}")
    public Response getRepairsByRangeOfDates(
            @PathParam("startDate") String startDateStr,
            @PathParam("endDate") String endDateStr,
            User user) {
        List<Repair> repairs = repairService.getRepairsByRangeOfDates(startDateStr, endDateStr, user);
        return Response.ok(repairs).build();
    }
}
