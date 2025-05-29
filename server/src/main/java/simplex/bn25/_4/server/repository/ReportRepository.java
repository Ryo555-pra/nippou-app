
package simplex.bn25._4.server.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import simplex.bn25._4.server.model.Report;
import simplex.bn25._4.server.model.ReportStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReportRepository {
    private final JdbcTemplate jdbc;

    public ReportRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Report> ROW_MAPPER = new RowMapper<>() {
        @Override
        public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
            Report r = new Report();
            r.setId(rs.getLong("id"));
            r.setHrid(rs.getString("hrid"));
            r.setReportDate(rs.getObject("report_date", LocalDate.class));
            r.setCurriculum(rs.getString("curriculum"));
            r.setY(rs.getString("y"));
            r.setW(rs.getString("w"));
            r.setT(rs.getString("t"));
            r.setStatus(ReportStatus.valueOf(rs.getString("status")));
            return r;
        }
    };

    /**
     * HRID＋日付でレポートを探す
     */
    public Optional<Report> findByHridAndReportDate(String hrid, LocalDate reportDate) {
        String sql = "SELECT id, hrid, report_date, curriculum, y, w, t, status " +
                "FROM reports " +
                "WHERE hrid = ? AND report_date = ?";
        List<Report> list = jdbc.query(sql, ROW_MAPPER, hrid, reportDate);
        return list.stream().findFirst();
    }

    /**
     * HRID＋日付範囲でレポート一覧を取得（日付昇順）
     */
    public List<Report> findAllByHridAndReportDateBetweenOrderByReportDateAsc(
            String hrid,
            LocalDate start,
            LocalDate end
    ) {
        String sql = "SELECT id, hrid, report_date, curriculum, y, w, t, status " +
                "FROM reports " +
                "WHERE hrid = ? AND report_date BETWEEN ? AND ? " +
                "ORDER BY report_date ASC";
        return jdbc.query(sql, ROW_MAPPER, hrid, start, end);
    }

    /**
     * 新規 INSERT
     */
    public void insertReport(Report r) {
        String sql = "INSERT INTO reports " +
                "(hrid, report_date, curriculum, y, w, t, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                r.getHrid(),
                r.getReportDate(),
                r.getCurriculum(),
                r.getY(),
                r.getW(),
                r.getT(),
                r.getStatus().name()
        );
    }

    /**
     * 更新 UPDATE
     */
    public void updateReport(Report r) {
        String sql = "UPDATE reports SET curriculum = ?, y = ?, w = ?, t = ?, status = ? " +
                "WHERE hrid = ? AND report_date = ?";
        jdbc.update(sql,
                r.getCurriculum(),
                r.getY(),
                r.getW(),
                r.getT(),
                r.getStatus().name(),
                r.getHrid(),
                r.getReportDate()
        );
    }

    /**
     * save: 存在すれば update, しなければ insert
     */
    public Report saveReport(Report r) {
        Optional<Report> existing = findByHridAndReportDate(r.getHrid(), r.getReportDate());
        if (existing.isPresent()) {
            updateReport(r);
        } else {
            insertReport(r);
        }
        return r;
    }
}
