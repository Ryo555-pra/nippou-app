package simplex.bn25._4.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import simplex.bn25._4.server.model.ReportStatus;

import java.time.LocalDate;

/**
 * レスポンス/リクエスト用DTO（イミュータブルなクラス）
 */
public final class ReportDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate reportDate;
    private final String curriculum;
    private final String y;
    private final String w;
    private final String t;
    private final ReportStatus status;

    public ReportDTO(LocalDate reportDate, String curriculum, String y, String w, String t, ReportStatus status) {
        this.reportDate = reportDate;
        this.curriculum = curriculum;
        this.y = y;
        this.w = w;
        this.t = t;
        this.status = status;
    }

    public LocalDate getReportDate() { return reportDate; }
    public String getCurriculum() { return curriculum; }
    public String getY() { return y; }
    public String getW() { return w; }
    public String getT() { return t; }
    public ReportStatus getStatus() { return status; }
}

