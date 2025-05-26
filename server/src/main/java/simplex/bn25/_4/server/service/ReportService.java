package simplex.bn25._4.server.service;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import simplex.bn25._4.server.model.Report;
//import simplex.bn25._4.server.model.ReportStatus;
//import simplex.bn25._4.server.repository.ReportRepository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//@Service
//public class ReportService {
//
//    @Autowired
//    private final ReportRepository reportRepository;
//
//    public ReportService(ReportRepository reportRepository) {
//        this.reportRepository = reportRepository;
//    }
//
//    public Optional<Report> getReport(String hrid, LocalDate date) {
//        return reportRepository.findByHridAndReportDate(hrid, date);
//    }
//
//    public Report submitReport(Report report) {
//        report.setCreatedAt(LocalDateTime.now());
//        return reportRepository.save(report);
//    }
//
//    /**
//     * 指定ユーザー・指定日の日報を保存または更新します。
//     * 既に存在する場合はそのレコードを更新し、存在しなければ新規作成します。
//     *
//     * @param hrId        認証済みユーザーの HRID
//     * @param reportDate  日報の日付
//     * @param curriculum  カリキュラム欄のテキスト
//     * @param y
//     * @param w
//     * @param t YWT フォーマット欄のテキスト
//     * @param status      日報のステータス（NOT_SUBMITTED, SAVED_TEMPORARILY, SUBMITTED）
//     * @return 保存後の Report エンティティ
//     */
//    public Report saveReport(
//            String hrId,
//            LocalDate reportDate,
//            String curriculum,
//            String y,
//            String w,
//            String t,
//            ReportStatus status
//    ) {
//        // 1) 既存レポートを取得、なければ新規インスタンスを準備
//        Report report = reportRepository
//                .findByHridAndReportDate(hrId, reportDate)
//                .orElseGet(() -> {
//                    Report r = new Report();
//                    r.setHrid(hrId);
//                    r.setReportDate(reportDate);
//                    return r;
//                });
//
//        // 2) フィールドを上書き
//        report.setCurriculum(curriculum);
//        report.setY(y);
//        report.setY(w);
//        report.setY(t);
//        report.setStatus(status);
//
//        // 3) DB に保存（INSERT or UPDATE）
//        return reportRepository.save(report);
//    }
//}
//





import org.springframework.stereotype.Service;
import simplex.bn25._4.server.model.Report;
import simplex.bn25._4.server.model.ReportStatus;
import simplex.bn25._4.server.repository.ReportRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final ReportRepository repo;

    public ReportService(ReportRepository repo) {
        this.repo = repo;
    }

//    /**
//     * 日報を新規 or 更新して保存します。
//     */
    public Report saveReport(
            String hrid,
            LocalDate reportDate,
            String curriculum,
            String y,
            String w,
            String t,
            ReportStatus status
    ) {
        Report rpt = repo.findByHridAndReportDate(hrid, reportDate)
                .orElseGet(() -> {
                    Report r = new Report();
                    r.setHrid(hrid);
                    r.setReportDate(reportDate);
                    return r;
                });

        rpt.setCurriculum(curriculum);
        rpt.setY(y);
        rpt.setW(w);
        rpt.setT(t);
        rpt.setStatus(status);

        return repo.save(rpt);
    }


    public List<Report> findLastWeek(String hrid) {
        LocalDate end   = LocalDate.now();
        LocalDate start = end.minusDays(6); // 今日を含む過去7日
        return repo.findAllByHridAndReportDateBetweenOrderByReportDateAsc(hrid, start, end);
    }

//    /**
//     * 日報を保存または更新します。
//     * （既存の saveReport メソッドなどがある場合は省略可）
//     */
//    public Report saveReport(
//            String hrid,
//            LocalDate reportDate,
//            String curriculum,
//            String y,
//            String w,
//            String t,
//            ReportStatus status
//    ) {
//        // 省略: DTO→Entity変換、Repository.save() 呼び出し など
//    }

    /**
     * 指定したユーザー(hrid)と日付のレポートを取得します。
     * 見つかれば Optional.of(report)、なければ Optional.empty() が返ります。
     */
    public Optional<Report> getReport(String hrid, LocalDate date) {
        return repo.findByHridAndReportDate(hrid, date);
    }

//    /**
//     * 過去1週間分などを一覧取得するメソッド例。
//     */
//    public List<Report> findAllByHridAndDateBetween(String hrid, LocalDate start, LocalDate end) {
//        return repo.findAllByHridAndReportDateBetweenOrderByReportDateAsc(hrid, start, end);
//    }
//
//    /**
//     * 指定した HRID かつ reportDate が start〜end の範囲内（両端 inclusive）
//     * のレポートを取得し、日付昇順で返します。
//     */
//    public List<Report> findAllByHridAndDateBetween(String hrid, LocalDate start, LocalDate end) {
//        return repo.findAllByHridAndReportDateBetweenOrderByReportDateAsc(hrid, start, end);
//    }

    /**
     * 指定ユーザーの、reportDate が start〜end の範囲内のレポートを取得。
     * (両端 inclusive, 日付昇順)
     */
    public List<Report> findAllByHridAndDateBetween(
            String hrid,
            LocalDate start,
            LocalDate end
    ) {
        return repo.findAllByHridAndReportDateBetweenOrderByReportDateAsc(hrid, start, end);
    }
}
