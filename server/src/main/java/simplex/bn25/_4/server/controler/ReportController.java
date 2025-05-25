package simplex.bn25._4.server.controler;//package simplex.bn25._4.server.controler;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import simplex.bn25._4.server.model.Report;
//import simplex.bn25._4.server.model.ReportStatus;
//import simplex.bn25._4.server.repository.ReportRepository;
//import simplex.bn25._4.server.service.ReportService;
//
//import java.security.Principal;
//import java.time.LocalDate;
//
//@RestController
//@RequestMapping("/api/reports")
//public class ReportController {
//
//    @Autowired
//    private final ReportService reportService;
//    private final ReportRepository reportRepository; // ✅ これを追加
//
//    public ReportController(ReportService reportService, ReportRepository reportRepository) {
//        this.reportService = reportService;
//        this.reportRepository = reportRepository;
//    }
//
//    @GetMapping("/whoami")
//    public ResponseEntity<String> whoAmI(Authentication authentication) {
//        return ResponseEntity.ok(authentication.getName()); // → "trainee01"
//    }
//
//    @PostMapping
//    public ResponseEntity<Report> postReport(
//            @AuthenticationPrincipal Principal principal,
//            @RequestBody ReportDTO dto) {
//        // HRID はここで取り出す
//        String hrId = principal.getName();  // JWT の subject として埋め込まれた HRID
//        Report saved = reportService.saveReport(
//                hrId,
//                dto.reportDate(),
//                dto.curriculum(),
//                dto.y(),
//                dto.w(),
//                dto.t(),
//                dto.status()
//        );
//        return ResponseEntity.ok(saved);
//    }
//
//    // リクエスト側 DTO から hrid フィールドを完全に削除する
//    public static record ReportDTO(
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
//            String curriculum,
//            String y,
//            String w,
//            String t,
//            ReportStatus status
//    ) {}
//
//
//    @GetMapping
//    public ResponseEntity<?> getReport(
//            @RequestParam String hrid,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        return reportService.getReport(hrid, date)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
////    @PostMapping
////    public ResponseEntity<Report> submitReport(@RequestBody Report report) {
////        return ResponseEntity.ok(reportService.submitReport(report));
////    }
//}
//


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import simplex.bn25._4.server.model.Report;
import simplex.bn25._4.server.model.ReportStatus;
import simplex.bn25._4.server.service.ReportService;

import java.security.Principal;
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
     * React からの POST /api/reports
     * JSON のボディは ReportRequestDTO にマッピングされる
     */
    @PostMapping
    public ResponseEntity<Report> createReport(
            @AuthenticationPrincipal Principal principal,
            @RequestBody ReportRequestDTO dto
    ) {
        String hrid = principal.getName();  // JWT subject から HRID を取得
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

        /** yyyy-MM-dd 形式の文字列を LocalDate に変換 */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate reportDate;

        private String curriculum;
        private String y;
        private String w;
        private String t;
        private ReportStatus status;

        // Jackson のために no-args コンストラクタが必要
        public ReportRequestDTO() {}

        // --- getters & setters ---
        public LocalDate getReportDate() { return reportDate; }
        public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
        public String getCurriculum() { return curriculum; }
        public void setCurriculum(String curriculum) { this.curriculum = curriculum; }
        public String getY() { return y; }
        public void setY(String y) { this.y = y; }
        public String getW() { return w; }
        public void setW(String w) { this.w = w; }
        public String getT() { return t; }
        public void setT(String t) { this.t = t; }
        public ReportStatus getStatus() { return status; }
        public void setStatus(ReportStatus status) { this.status = status; }
    }


//    @GetMapping("/last-week")
//    public ResponseEntity<List<ReportSummaryDTO>> lastWeek(
//            @AuthenticationPrincipal Principal principal
//    ) {
//        String hrid = principal.getName();
//        var list = service.findLastWeek(hrid)
//                .stream()
//                // T だけと日付だけを返す DTO に変換
//                .map(r -> new ReportSummaryDTO(r.getReportDate(), r.getT()))
//                .toList();
//        return ResponseEntity.ok(list);
//    }
//
//    public static record ReportSummaryDTO(
//            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate reportDate,
//            String t
//    ) {}
//@GetMapping("/last-week")
//public ResponseEntity<List<ReportSummaryDTO>> lastWeek(Authentication authentication) {
//    // Authentication オブジェクトは必ず入るので null にはなりません
//    String hrid = authentication.getName();
//
//    LocalDate end   = LocalDate.now();
//    LocalDate start = end.minusDays(6);
//    List<ReportSummaryDTO> list =
//            service.findAllByHridAndDateBetween(hrid, start, end)
//                    .stream()
//                    .map(r -> new ReportSummaryDTO(r.getReportDate(), r.getT()))
//                    .toList();
//
//    return ResponseEntity.ok(list);
//}
@GetMapping("/last-week")
public ResponseEntity<List<ReportSummaryDTO>> lastWeek(Authentication authentication) {
    // Authentication は必ず注入されるので NPE は避けられます
    String hrid = authentication.getName();

    LocalDate end   = LocalDate.now();
    LocalDate start = end.minusDays(6);
    List<Report> reports = service.findAllByHridAndDateBetween(hrid, start, end);

    List<ReportSummaryDTO> summary = reports.stream()
            .map(r -> new ReportSummaryDTO(r.getReportDate(), r.getT()))
            .toList();

    return ResponseEntity.ok(summary);
}


//    public record ReportSummaryDTO(
//            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate reportDate,
//            String t
//    ) {}
}

