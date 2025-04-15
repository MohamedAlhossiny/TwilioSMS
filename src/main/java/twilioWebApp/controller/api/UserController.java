package twilioWebApp.controller.api;

import java.util.stream.Collectors;

import org.hibernate.HibernateException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import twilioWebApp.model.TwilioAccount;
import twilioWebApp.model.User;
import twilioWebApp.service.Impl.TwilioServiceImpl;
import twilioWebApp.service.Impl.UserServiceImpl;
import twilioWebApp.service.UserService;

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
                return Response.ok(userService.findAll().stream().filter(user -> !user.getRole().equals("admin")).collect(Collectors.toList())).build();
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

    @GET
    @Path("/checkSession")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSession() {//tested
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = userService.findById((Integer) session.getAttribute("id"));
            return Response.ok(user).build(); // Session valid
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build(); // Session expired
        }
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {//tested
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();   
        }
        return Response.ok().build();
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
        User existingUser = userService.findById(id);
        Integer userId = (Integer) session.getAttribute("id");
        if (role.equals("admin") || userId.equals(id)) {
            user.setRole("customer");
            user.setId(id);
            user.setPasswd(existingUser.getPasswd());
            user.setUsername(existingUser.getUsername());
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