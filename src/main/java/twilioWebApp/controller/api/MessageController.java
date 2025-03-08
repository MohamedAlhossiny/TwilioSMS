package twilioWebApp.controller.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import twilioWebApp.model.OutboundMsg;
import twilioWebApp.service.Impl.TwilioServiceImpl;
import jakarta.ws.rs.Consumes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import jakarta.ws.rs.core.Context;
import java.util.List;
import java.util.Date;
import twilioWebApp.service.Impl.OutMessageServiceImpl; 
import twilioWebApp.model.TwilioAccount;

@Path("/message")
public class MessageController {
    private OutMessageServiceImpl outMessageService = new OutMessageServiceImpl();
    @Context
    private HttpServletRequest request;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendSMS(OutboundMsg msg) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        Integer userId = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        Boolean isVerified = (Boolean) session.getAttribute("isVerified");
        
        if(!role.equals("customer") || !isVerified) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Only verified customers that have a twilio account can send SMS").build();
        }
        msg.setUser_id(userId);
        msg.setMsg_date(new Date());

        TwilioServiceImpl twilioService = new TwilioServiceImpl();
        try {
            TwilioAccount twilio = twilioService.findByUserId(userId);
            twilio.setNumber_of_msg(twilio.getNumber_of_msg() + 1);
            twilioService.update(twilio);
            twilioService.sendSms(msg);
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        outMessageService.save(msg);
        return Response.ok(msg).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessages() {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        String role = (String) session.getAttribute("role");
        if(!role.equals("admin")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Only admins can send SMS").build();
        }
        List<OutboundMsg> messages = outMessageService.findAll();
        return Response.ok(messages).build();
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
        public Response getMessagesByUserId(@PathParam("userId") Integer userId) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        Integer loggedInUserId = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        if(!role.equals("admin") && loggedInUserId != userId) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Only admins or owner can access this resource").build();
        }
        List<OutboundMsg> messages = outMessageService.findByUserId(userId);
        return Response.ok(messages).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMessage(@PathParam("id") Integer id) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        try {
        Integer userId = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        OutboundMsg outboundMsg = outMessageService.findById(id);
        if(outboundMsg == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Message not found").build();
        }
        if(!role.equals("admin") && userId != outboundMsg.getUser_id()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Only admins can delete messages").build();
        }
            outMessageService.delete(id);
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.ok("Message deleted").build();
    }

    
}
