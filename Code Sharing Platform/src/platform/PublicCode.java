package platform;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Public")
public class PublicCode implements Code {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String uuid;
    @Column(columnDefinition = "text")
    private String code;
    @Column
    private int time;
    @Column
    private int views;

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @CreatedDate
    private Timestamp createdAt;

    public void setDate(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDate() {
        return createdAt.toString();
    }

}

