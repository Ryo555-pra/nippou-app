package simplex.bn25._4.server.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import simplex.bn25._4.server.model.TaskEvaluation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskEvaluationRepository
        extends JpaRepository<TaskEvaluation, Long> {

    Optional<TaskEvaluation> findByHridAndReportDateAndTaskIndex(
            String hrid, LocalDate reportDate, int taskIndex
    );

    List<TaskEvaluation> findAllByHridAndReportDateBetweenOrderByReportDateAscTaskIndexAsc(
            String hrid, LocalDate start, LocalDate end
    );
}
