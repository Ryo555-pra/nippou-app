package simplex.bn25._4.server.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import simplex.bn25._4.server.model.Report;
import simplex.bn25._4.server.model.ReportStatus;
import simplex.bn25._4.server.repository.ReportRepository;
import simplex.bn25._4.server.service.ReportService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private final ReportService reportService;
    private final ReportRepository reportRepository; // ✅ これを追加

    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/whoami")
    public ResponseEntity<String> whoAmI(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName()); // → "trainee01"
    }

//    @PostMapping
//    public ResponseEntity<Report> submitReport(@RequestBody Report report, Authentication auth) {
//        String hrid = auth.getName(); // ← ログインユーザー名
//        report.setHrid(hrid);
//        report.setCreatedAt(LocalDateTime.now());
//        return ResponseEntity.ok(reportRepository.save(report));
//    }

    @PostMapping
    public ResponseEntity<Report> postReport(
            @AuthenticationPrincipal Principal principal,
            @RequestBody ReportDTO dto) {
        // HRID はここで取り出す
        String hrId = principal.getName();  // JWT の subject として埋め込まれた HRID
        Report saved = reportService.saveReport(
                hrId,
                dto.reportDate(),
                dto.curriculum(),
                dto.y(),
                dto.w(),
                dto.t(),
                dto.status()
        );
        return ResponseEntity.ok(saved);
    }

    // リクエスト側 DTO から hrid フィールドを完全に削除する
    public static record ReportDTO(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
            String curriculum,
            String y,
            String w,
            String t,
            ReportStatus status
    ) {}


    @GetMapping
    public ResponseEntity<?> getReport(
            @RequestParam String hrid,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportService.getReport(hrid, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping
//    public ResponseEntity<Report> submitReport(@RequestBody Report report) {
//        return ResponseEntity.ok(reportService.submitReport(report));
//    }
}

