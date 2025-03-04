package twilioWebApp.model;

import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "verificationcode")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "verification_code")
    private Integer verification_code;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "expired_date")
    private Date expired_date;

    @Column(name = "created_at")
    private Date created_at;

    public VerificationCode() {
    }

    public VerificationCode(Integer id, Integer verification_code, Integer user_id, Date expired_date, Date created_at) {
        this.id = id;
        this.verification_code = verification_code;
        this.user_id = user_id;
        this.expired_date = expired_date;
        this.created_at = created_at;
    }
    public VerificationCode(Integer verification_code, Date expired_date, Integer user_id) {
        this.user_id = user_id;
        this.verification_code = verification_code;
        this.expired_date = expired_date;
    }

    public Integer getId() {
        return id;
    }

    public Integer getVerification_code() {
        return verification_code;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Date getExpired_date() {
        return expired_date;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setId(Integer id) {
        this.id = id;
    }       

    public void setVerification_code(Integer verification_code) {
        this.verification_code = verification_code;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }   

    public void setExpired_date(Date expired_date) {
        this.expired_date = expired_date;
    }   

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    
    
}
