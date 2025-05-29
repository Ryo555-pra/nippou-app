package simplex.bn25._4.server.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * レスポンス用 DTO（イミュータブルなクラス）
 **/
public final class SummaryWithScoreDTO {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate reportDate;
    private final int taskIndex;
    private final int score;

    public SummaryWithScoreDTO(LocalDate reportDate, int taskIndex, int score) {
        this.reportDate = reportDate;
        this.taskIndex = taskIndex;
        this.score = score;
    }

    public LocalDate getReportDate() { return reportDate; }
    public int getTaskIndex() { return taskIndex; }
    public int getScore() { return score; }
}
