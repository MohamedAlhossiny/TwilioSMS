package twilioWebApp.model;

import jakarta.persistence.*; 

@Entity
@Table(name = "twilioaccount")
public class TwilioAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sid")
    private String sid;

    @Column(name = "token")
    private String token;

    @Column(name = "sender_id")
    private String sender_id;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "isVerified", nullable = false)
    private Boolean isVerified;
    
    public TwilioAccount() {
    }

    public TwilioAccount(Integer id, String sid, String token, String sender_id, String phone_number, Integer user_id) {
        this.id = id;
        this.sid = sid;
        this.token = token;
        this.sender_id = sender_id; 
        this.phone_number = phone_number;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public String getSid() {
        return sid;
    }

    public String getToken() {
        return token;
    }

    public String getSender_id() {
        return sender_id;
    }       

    public String getPhone_number() {
        return phone_number;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
