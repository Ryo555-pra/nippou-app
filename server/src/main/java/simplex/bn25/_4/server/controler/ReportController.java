package simplex.bn25._4.server.controler;//package simplex.bn25._4.server.controler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import simplex.bn25._4.server.model.Report;
import simplex.bn25._4.server.model.ReportStatus;
import simplex.bn25._4.server.model.TaskEvaluation;
import simplex.bn25._4.server.service.ReportService;
import simplex.bn25._4.server.service.TaskEvaluationService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService service;
    private final TaskEvaluationService evalService;

    public ReportController(ReportService service, TaskEvaluationService evalService) {
        this.service = service;
        this.evalService = evalService;
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

    // タスク一覧（改行つき文字列 t を返す）
    @GetMapping("/last-week/t")
    public List<ReportSummaryDTO> lastWeekTasks(Authentication auth) {
        String hrid = auth.getName();
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        return service.findAllByHridAndDateBetween(hrid, start, end).stream()
                .map(r -> new ReportSummaryDTO(r.getReportDate(), null, r.getT()))
                .toList();
    }

    /**
     * ① タスクごとの自己評価を保存する
     */
    @PostMapping("/{date}/scores")
    public ResponseEntity<Void> saveScores(
            Authentication auth,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody List<TaskEvaluationService.TaskScoreDTO> scores
    ) {
        String hrid = auth.getName();
        evalService.saveEvaluations(hrid, date, scores);
        return ResponseEntity.ok().build();
    }

    /**
     * ② 過去一週間分のタスク評価を取得して返す
     */
    @GetMapping("/last-week/tasks")
    public ResponseEntity<List<SummaryWithScoreDTO>> lastWeekWithScores(
            Authentication auth
    ) {
        String hrid = auth.getName();
        List<TaskEvaluation> evs = evalService.findLastWeekEvaluations(hrid);

        // DTO に詰め替え
        List<SummaryWithScoreDTO> list = evs.stream()
                .map(ev -> new SummaryWithScoreDTO(
                        ev.getReportDate(),
                        ev.getTaskIndex(),
                        ev.getScore()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    /**
     * レスポンス用 DTO
     **/
    public static record SummaryWithScoreDTO(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
            int taskIndex,
            int score
    ) {
    }
}

