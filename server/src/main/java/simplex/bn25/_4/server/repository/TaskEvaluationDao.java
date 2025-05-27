package simplex.bn25._4.server.repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import simplex.bn25._4.server.model.TaskEvaluation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TaskEvaluationDao {
    private final JdbcTemplate jdbc;

    public TaskEvaluationDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<TaskEvaluation> ROW_MAPPER = new RowMapper<>() {
        @Override
        public TaskEvaluation mapRow(ResultSet rs, int rowNum) throws SQLException {
            TaskEvaluation e = new TaskEvaluation();
            e.setHrid(rs.getString("hrid"));
            e.setReportDate(rs.getObject("report_date", LocalDate.class));
            e.setTaskIndex(rs.getInt("task_index"));
            e.setScore(rs.getInt("score"));
            return e;
        }
    };

    /**
     * upsert (PostgreSQL) / insert or update score
     */
    public void upsertEvaluation(String hrid, LocalDate reportDate, int taskIndex, int score) {
        String sql = "INSERT INTO task_evaluations (hrid, report_date, task_index, score) " +
                "VALUES (?, ?, ?, ?) " +
                "ON CONFLICT (hrid, report_date, task_index) " +
                "DO UPDATE SET score = EXCLUDED.score";
        jdbc.update(sql, hrid, reportDate, taskIndex, score);
    }

    /**
     * 過去1週間分取得
     */
    public List<TaskEvaluation> findBetween(String hrid, LocalDate start, LocalDate end) {
        String sql = "SELECT hrid, report_date, task_index, score " +
                "FROM task_evaluations " +
                "WHERE hrid = ? AND report_date BETWEEN ? AND ? " +
                "ORDER BY report_date ASC, task_index ASC";
        return jdbc.query(sql, ROW_MAPPER, hrid, start, end);
    }
}
