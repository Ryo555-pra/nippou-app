package simplex.bn25._4.server.controler;//package simplex.bn25._4.server.controler;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import simplex.bn25._4.server.dto.ReportRequestDTO;
import simplex.bn25._4.server.dto.ReportSummaryDTO;
import simplex.bn25._4.server.dto.SummaryWithScoreDTO;
import simplex.bn25._4.server.model.Report;
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
        // ServiceにDTOとhridを渡すだけにする
        Report saved = service.saveReport(dto, hrid);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/last-week")
    public ResponseEntity<List<ReportSummaryDTO>> lastWeek(Authentication authentication) {
        String hrid = authentication.getName();
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        // ServiceはEntityリストを返す。DTO変換はControllerで行う
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
        // ServiceはEntityリストを返す。DTO変換はControllerで行う
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
}

