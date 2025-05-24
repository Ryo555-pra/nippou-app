package simplex.bn25._4.server.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hrid;

    private LocalDate reportDate;

    @Column(length = 3000)
    private String curriculum;

    @Column(length = 1000)
    private String y;

    @Column(length = 1000)
    private String w;

    @Column(length = 1000)
    private String t;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.NOT_SUBMITTED;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHrid() {
        return hrid;
    }

    public void setHrid(String hrid) {
        this.hrid = hrid;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
