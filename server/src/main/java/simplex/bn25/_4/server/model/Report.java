package simplex.bn25._4.server.model;

//import jakarta.persistence.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
///**
// * 日報エンティティ
// */
//@Entity
//@Table(name = "reports")
//public class Report {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String hrid;              // トレーニーのHRID
//
//    @Column(nullable = false)
//    private LocalDate reportDate;     // 日報の日付
//
//    @Column(columnDefinition = "TEXT")
//    private String curriculum;        // カリキュラム記入欄
//
//    @Column(columnDefinition = "TEXT")
//    private String y;                 // Y: やったこと
//
//    @Column(columnDefinition = "TEXT")
//    private String w;                 // W: わかったこと
//
//    @Column(columnDefinition = "TEXT")
//    private String t;                 // T: 次にすること
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private ReportStatus status;      // ステータス
//
//    private LocalDateTime createdAt;
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//// --- Getter / Setter ---
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getHrid() {
//        return hrid;
//    }
//
//    public void setHrid(String hrid) {
//        this.hrid = hrid;
//    }
//
//    public LocalDate getReportDate() {
//        return reportDate;
//    }
//
//    public void setReportDate(LocalDate reportDate) {
//        this.reportDate = reportDate;
//    }
//
//    public String getCurriculum() {
//        return curriculum;
//    }
//
//    public void setCurriculum(String curriculum) {
//        this.curriculum = curriculum;
//    }
//
//    public String getY() {
//        return y;
//    }
//
//    public void setY(String y) {
//        this.y = y;
//    }
//
//    public String getW() {
//        return w;
//    }
//
//    public void setW(String w) {
//        this.w = w;
//    }
//
//    public String getT() {
//        return t;
//    }
//
//    public void setT(String t) {
//        this.t = t;
//    }
//
//    public ReportStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(ReportStatus status) {
//        this.status = status;
//    }
//}
//
//

// src/main/java/com/example/reportapp/model/Report.java


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** トレーニーの HRID (JWT の subject) */
    @Column(nullable = false)
    private String hrid;

    /** 日報の日付 */
    @Column(nullable = false)
    private LocalDate reportDate;

    /** カリキュラム記入欄 */
    @Column(columnDefinition = "TEXT")
    private String curriculum;

    /** Y: やったこと */
    @Column(columnDefinition = "TEXT")
    private String y;

    /** W: わかったこと */
    @Column(columnDefinition = "TEXT")
    private String w;

    /** T: 次にすること */
    @Column(columnDefinition = "TEXT")
    private String t;

    /** ステータス */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    // --- getters / setters omitted for brevity ---

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

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
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
}

