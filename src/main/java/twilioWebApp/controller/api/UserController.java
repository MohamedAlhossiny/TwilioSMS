package twilioWebApp.controller.api;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import twilioWebApp.model.User;
import twilioWebApp.service.UserService;
import twilioWebApp.service.Impl.UserServiceImpl;
import org.hibernate.HibernateException;
import twilioWebApp.service.Impl.TwilioServiceImpl;
import twilioWebApp.model.TwilioAccount;

@Path("/user")
public class UserController {

    private UserService userService;
    
    @Context
    private HttpServletRequest request;

    public UserController() {
        this.userService = new UserServiceImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(){
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        String role = (String) session.getAttribute("role");
        try {
            if (role.equals("admin")) {
                return Response.ok(userService.findAll()).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("You are not authorized to access this resource").build();
            }
        } catch (HibernateException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Integer userId) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        String role = (String) session.getAttribute("role");
        if (role.equals("admin") || session.getAttribute("id").equals(userId)) {
            return Response.ok(userService.findById(userId)).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("You are not authorized to access this resource").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {//tested
        try {
            user.setRole("customer");
            user.setId(null);
            userService.register(user);
            return Response.ok(user).build();
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user) {//tested
        User retUser = null;
        try {
            retUser = userService.authenticate(user.getUsername(), user.getPasswd());
            if (retUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("Invalid username or password").build();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("role", retUser.getRole());
            session.setAttribute("id", retUser.getId());
            TwilioAccount userData = new TwilioServiceImpl().findByUserId(retUser.getId());
            if(userData != null){
                session.setAttribute("isVerified", userData.getIsVerified());
            }else{
                session.setAttribute("isVerified", false);//if it's the first time the user logs in, it will be false
            }
            return Response.ok(retUser).build();

        } catch (HibernateException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Integer id, User user) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("id");
        if (role.equals("admin") || userId.equals(id)) {
            user.setRole("customer");
            user.setId(id);
            userService.update(user);
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("You are not authorized to access this resource").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Integer id) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("You must be logged in to access this resource").build();
        }
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("id");
        if (role.equals("admin") || userId.equals(id)) {
            userService.delete(id);
            return Response.ok("User deleted").build();
        } else {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("You are not authorized to access this resource").build();
        }
    }

}