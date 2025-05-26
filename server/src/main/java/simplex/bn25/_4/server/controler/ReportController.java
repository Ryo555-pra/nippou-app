package simplex.bn25._4.server.controler;//package simplex.bn25._4.server.controler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import simplex.bn25._4.server.model.Report;
import simplex.bn25._4.server.model.ReportStatus;
import simplex.bn25._4.server.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    /**
     * フロントの axios.get('/api/reports?date=…') をハンドルする
     * hrid は JWT/セッションから取ってしまう
     */
    @GetMapping  // ← pathは空、クエリパラdateだけを受ける
    public ResponseEntity<Report> getReport(
            Authentication auth,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String hrid = auth.getName();
        return service.getReport(hrid, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Report> createReport(
            Authentication auth,
            @RequestBody ReportRequestDTO dto
    ) {
        String hrid = auth.getName();
        Report saved = service.saveReport(
                hrid,
                dto.getReportDate(),
                dto.getCurriculum(),
                dto.getY(),
                dto.getW(),
                dto.getT(),
                dto.getStatus()
        );
        return ResponseEntity.ok(saved);
    }

    /**
     * リクエスト JSON 用 DTO
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReportRequestDTO {

        /**
         * yyyy-MM-dd 形式の文字列を LocalDate に変換
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate reportDate;

        private String curriculum;
        private String y;
        private String w;
        private String t;
        private ReportStatus status;

        // Jackson のために no-args コンストラクタが必要
        public ReportRequestDTO() {
        }

        // --- getters & setters ---
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


    @GetMapping("/last-week")
    public ResponseEntity<List<ReportSummaryDTO>> lastWeek(Authentication authentication) {
        // Authentication は必ず注入されるので NPE は避けられます
        String hrid = authentication.getName();

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        List<Report> reports = service.findAllByHridAndDateBetween(hrid, start, end);

        List<ReportSummaryDTO> summary = reports.stream()
                .map(r -> new ReportSummaryDTO(r.getReportDate(), r.getCurriculum(), null))
                .toList();

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/last-week/t")
    public ResponseEntity<List<ReportSummaryDTO>> tSummary(Authentication auth) {
        String hrid = auth.getName();
        List<ReportSummaryDTO> list = service.findLastWeek(hrid).stream()
                // t をセット、curriculum は null
                .map(r -> new ReportSummaryDTO(r.getReportDate(), null, r.getT()))
                .toList();
        return ResponseEntity.ok(list);
    }
}

