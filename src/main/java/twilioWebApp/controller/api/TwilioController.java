package twilioWebApp.controller.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import twilioWebApp.service.Impl.TwilioServiceImpl;
import org.hibernate.HibernateException;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import twilioWebApp.model.TwilioAccount;
import java.util.List;

@Path("/twilio")
public class TwilioController {

    private TwilioServiceImpl twilioService;

    @Context
    private HttpServletRequest request;

    public TwilioController() {
        this.twilioService = new TwilioServiceImpl();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerTwilio(TwilioAccount twilio) {// tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("You must be logged in to access this resource").build();
        }
        Integer userId = (Integer) session.getAttribute("id");
        if(userId != twilio.getUser_id()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Can't register twilio account for other user").build();
        }
        twilio.setIsVerified(false);
        twilio.setUser_id(userId);
        twilio.setNumber_of_msg(0);
        try {
            twilioService.save(twilio);
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok("Twilio account registered").build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTwilio(TwilioAccount twilio) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("You must be logged in to access this resource").build();
        }
        twilio.setIsVerified(false);
        Integer userId = (Integer) session.getAttribute("id");
        
        if(userId != twilio.getUser_id()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Only owner can update twilio account").build();
        }
        try {
        TwilioAccount twilioAccount = twilioService.findByUserId(userId);
        if(twilioAccount == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Twilio account not found to update").build();
        }
        twilio.setId(twilioAccount.getId());
        twilio.setNumber_of_msg(twilioAccount.getNumber_of_msg());
            twilioService.update(twilio);
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok("Twilio account updated").build();
    }

    @DELETE
    @Path("/{twilio_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTwilio(@PathParam("twilio_id") Integer twilio_id) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        Integer userId = (Integer) session.getAttribute("id");
        try {
            TwilioAccount twilio = twilioService.findById(twilio_id);
            if(twilio == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Twilio account not found to delete").build();
            }
            if(userId != twilio.getUser_id()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("Only owner can delete twilio account").build();
            }
            twilioService.delete(twilio_id);
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok("Twilio account deleted").build();
    }

    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTwilioAccounts() {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        String role = (String) session.getAttribute("role");
        if(!role.equals("admin")) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Only admin can get all twilio accounts").build();
        }
        try {
            List<TwilioAccount> twilios = twilioService.findAll();
            return Response.ok(twilios).build();
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTwilio(@PathParam("user_id") Integer user_id) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        Integer loggedInUserId = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        if(!role.equals("admin") && loggedInUserId != user_id) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Only admin or owner can get twilio account").build();
        }
        try {
            TwilioAccount twilio = twilioService.findByUserId(user_id);
            if(twilio == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("Twilio account not found").build();
            }
            return Response.ok(twilio).build();
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
