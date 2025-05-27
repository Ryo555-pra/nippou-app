package simplex.bn25._4.server.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "task_evaluations")
public class TaskEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // トレーニーID
    @Column(nullable = false)
    private String hrid;

    // 日付
    @Column(nullable = false)
    private LocalDate reportDate;

    // タスクのインデックス（Report.t の改行ごとの index）
    @Column(nullable = false)
    private int taskIndex;

    // 1～5 の評価
    @Column(nullable = false)
    private int score;

    // --- getters / setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHrid() { return hrid; }
    public void setHrid(String hrid) { this.hrid = hrid; }

    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }

    public int getTaskIndex() { return taskIndex; }
    public void setTaskIndex(int taskIndex) { this.taskIndex = taskIndex; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
