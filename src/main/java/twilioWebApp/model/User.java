package twilioWebApp.model;
import jakarta.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", length = 100)
    private String full_name;

    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String username;

    @Column(name = "passwd", length = 60)
    private String passwd;

    @Column(name = "job", length = 50)
    private String job;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birth_date;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "role", length = 50, nullable = false)
    private String role;



    public User() {
    }

    public User(Integer id, String full_name, String username, String passwd, String job, Date birth_date, 
                String email, String address, String role) {
        this.id = id;
        this.full_name = full_name;
        this.username = username;
        this.passwd = passwd;
        this.job = job;
        this.birth_date = birth_date;
        this.email = email;
        this.address = address;
        this.role = role;   
    }

    public Integer getId() {
        return id;
    }   

    public String getFull_name() {
        return full_name;
    }

    public String getUsername() {
        return username;    
    }   

    public String getPasswd() {
        return passwd;
    }

    public String getJob() {
        return job;
    }

    public Date getBirth_date() {
        return birth_date;
    }   

    public String getEmail() {
        return email;
    }

    public String getAddress() {    
        return address;
    }

    public String getRole() {
        return role;
    }   

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }   

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswd(String passwd) {  
        this.passwd = passwd;
    }

    public void setJob(String job) {
        this.job = job;
    }   

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public void setEmail(String email) {    
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }   

    public void setRole(String role) {
        this.role = role;
    }



}
