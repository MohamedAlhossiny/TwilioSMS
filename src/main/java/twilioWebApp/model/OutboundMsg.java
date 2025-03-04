package twilioWebApp.model;

import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "outboundmsg")
public class OutboundMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "from_num")
    private String from_num;

    @Column(name = "to_num")
    private String to_num;

    @Column(name = "body")
    private String body;

    @Column(name = "msg_date")
    private Date msg_date;

    @Column(name = "user_id")
    private Integer user_id;

    public OutboundMsg() {
    }

    public OutboundMsg(Integer id, String from_num, String to_num, String body, Date msg_date, Integer user_id) {
        this.id = id;
        this.from_num = from_num;
        this.to_num = to_num;
        this.body = body;
        this.msg_date = msg_date;
        this.user_id = user_id;
    }   

    public OutboundMsg(String to_num, String from_num, String body, Date msg_date, Integer user_id) {
        this.user_id = user_id;
        this.to_num = to_num;
        this.from_num = from_num;
        this.body = body;
        this.msg_date = msg_date;
    }

    public Integer getId() {
        return id;
    }       

    public String getFrom_num() {
        return from_num;
    }   

    public String getTo_num() {
        return to_num;
    }   

    public String getBody() {
        return body;
    }   

    public Date getMsg_date() {
        return msg_date;
    }   

    public Integer getUser_id() {
        return user_id;
    }      

    public void setId(Integer id) {
        this.id = id;
    }   

    public void setFrom_num(String from_num) {  
        this.from_num = from_num;
    }   

    public void setTo_num(String to_num) {
        this.to_num = to_num;
    }          

    public void setBody(String body) {
        this.body = body;
    }   

    public void setMsg_date(Date msg_date) {
        this.msg_date = msg_date;
    }   

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }   

}
