package simplex.bn25._4.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public final class ReportSummaryDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate reportDate;
    private final String curriculum;
    private final String t;

    public ReportSummaryDTO(LocalDate reportDate, String curriculum, String t) {
        this.reportDate = reportDate;
        this.curriculum = curriculum;
        this.t = t;
    }

    public LocalDate getReportDate() { return reportDate; }
    public String getCurriculum() { return curriculum; }
    public String getT() { return t; }
}
