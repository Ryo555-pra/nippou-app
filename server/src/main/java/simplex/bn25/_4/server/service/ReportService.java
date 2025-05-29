package simplex.bn25._4.server.service;

import org.springframework.stereotype.Service;
import simplex.bn25._4.server.dto.ReportRequestDTO;
import simplex.bn25._4.server.model.Report;
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

    /**
     * DTOとhridを受け取り、Service内でEntity変換して保存
     */
    public Report saveReport(ReportRequestDTO dto, String hrid) {
        Report rpt = repo.findByHridAndReportDate(hrid, dto.getReportDate())
                .orElseGet(() -> {
                    Report r = new Report();
                    r.setHrid(hrid);
                    r.setReportDate(dto.getReportDate());
                    return r;
                });
        rpt.setCurriculum(dto.getCurriculum());
        rpt.setY(dto.getY());
        rpt.setW(dto.getW());
        rpt.setT(dto.getT());
        rpt.setStatus(dto.getStatus());
        return repo.saveReport(rpt);
    }


    public List<Report> findLastWeek(String hrid) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6); // 今日を含む過去7日
        return repo.findAllByHridAndReportDateBetweenOrderByReportDateAsc(hrid, start, end);
    }

    /**
     * 指定したユーザー(hrid)と日付のレポートを取得します。
     * 見つかれば Optional.of(report)、なければ Optional.empty() が返ります。
     */
    public Optional<Report> getReport(String hrid, LocalDate date) {
        return repo.findByHridAndReportDate(hrid, date);
    }

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
