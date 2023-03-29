package sg.edu.nus.server.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="verify")
public class VerificationTask {
    @Id
    @Column(name="id")
    private String id;
    @Column(name="status")
    private String status;
    @Column(name="result")
    private String result;
    @Column(name="time")
    private Date time;


    protected VerificationTask() {}

    public VerificationTask(String id, String status, String result, Date time) {
        this.id = id;
        this.status = status;
        this.result = result;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        if(result != null) return result;
        else return "[]";
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
