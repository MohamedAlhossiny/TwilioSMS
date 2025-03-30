package twilioWebApp.controller.api;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import twilioWebApp.model.VerificationCode;
import twilioWebApp.model.OutboundMsg;
import twilioWebApp.service.TwilioService;
import twilioWebApp.service.Impl.TwilioServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import jakarta.servlet.http.HttpSession;
import twilioWebApp.model.TwilioAccount;
import twilioWebApp.service.VerificationCodeService;
import twilioWebApp.service.Impl.VerificationCodeServiceImpl;
import java.util.Date;
import org.hibernate.HibernateException;

@Path("/verificationCode")
public class VerificationController {
    
    private final TwilioService twilioService;
    private final VerificationCodeService verificationCodeService;
    
    @Context
    private HttpServletRequest request;
    
    public VerificationController() {
        // In a real application, these would be injected via dependency injection
        this.twilioService = new TwilioServiceImpl();
        this.verificationCodeService = new VerificationCodeServiceImpl();
    }

    @POST
    @Path("/send")
    public Response sendVerificationCode() {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                   .entity("You must be logged in to access this resource").build();
        }
        
        try {
            Integer userId = (Integer) session.getAttribute("id");
            TwilioAccount twilio = twilioService.findByUserId(userId);
            
            if (twilio == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                       .entity("This user does not have a Twilio account").build();
            }
            
            // Create verification code with 10 minute expiration
            Date expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 10);
            VerificationCode verificationCode = verificationCodeService.generateCode(userId, expirationTime);
            
            // Send SMS
            OutboundMsg msg = new OutboundMsg(
                twilio.getSender_id(),
                twilio.getPhone_number(),
                "Your Twilio verification code is: " + verificationCode.getVerification_code(),
                new Date(),
                userId
            );
            
            twilioService.sendSms(msg);
            verificationCodeService.save(verificationCode);
            
            return Response.ok("Verification code sent").build();
            
        } catch (HibernateException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                   .entity("An error occurred while processing your request: " + e.getMessage())
                   .build();
        }
    }

    @POST
    @Path("/verify")
    public Response verifyVerificationCode(VerificationCode verificationCode) {//tested
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                   .entity("You must be logged in to access this resource").build();
        }
        
        try {
            Integer userId = (Integer) session.getAttribute("id");
            boolean isVerified = verificationCodeService.verifyCode(userId, 
                                    verificationCode.getVerification_code());
            
            if (!isVerified) {
                return Response.status(Response.Status.BAD_REQUEST)
                       .entity("Invalid verification code").build();
            }
            
            // Update session and clean up verification codes
            session.setAttribute("isVerified", true);
            verificationCodeService.deleteAllCodesByUser(userId);
            
            // Update Twilio account verification status
            TwilioAccount twilio = twilioService.findByUserId(userId);
            twilio.setIsVerified(true);
            twilioService.update(twilio);
            
            return Response.ok("Verification code verified").build();
            
        } catch (HibernateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                   .entity("An error occurred while processing your request: " + e.getMessage())
                   .build();
        }
    }
}
