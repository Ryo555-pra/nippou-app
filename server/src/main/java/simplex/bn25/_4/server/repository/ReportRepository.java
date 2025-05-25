package simplex.bn25._4.server.repository;

//import org.springframework.data.jpa.repository.JpaRepository;
//import simplex.bn25._4.server.model.Report;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public interface ReportRepository extends JpaRepository<Report, Long> {
//
//    Optional<Report> findByHridAndReportDate(String hrid, LocalDate reportDate);
//
//    List<Report> findAllByHrid(String hrid);
//}





import org.springframework.data.jpa.repository.JpaRepository;
import simplex.bn25._4.server.model.Report;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    /** HRID＋日付でレポートを探す */
    Optional<Report> findByHridAndReportDate(String hrid, LocalDate reportDate);


    // ← ここを追加
    List<Report> findAllByHridAndReportDateBetweenOrderByReportDateAsc(
            String hrid,
            LocalDate start,
            LocalDate end
    );
}
