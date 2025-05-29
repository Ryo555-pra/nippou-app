package simplex.bn25._4.server.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public final class TaskScoreDTO {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate reportDate;
    private final int taskIndex;
    private final int score;

    public TaskScoreDTO(LocalDate reportDate, int taskIndex, int score) {
        this.reportDate = reportDate;
        this.taskIndex = taskIndex;
        this.score = score;
    }

    public LocalDate getReportDate() { return reportDate; }
    public int getTaskIndex() { return taskIndex; }
    public int getScore() { return score; }
}

