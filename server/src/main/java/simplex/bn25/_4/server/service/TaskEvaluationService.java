package simplex.bn25._4.server.service;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import simplex.bn25._4.server.model.TaskEvaluation;
import simplex.bn25._4.server.repository.TaskEvaluationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TaskEvaluationService {

    private final TaskEvaluationRepository repo;

    public TaskEvaluationService(TaskEvaluationRepository repo) {
        this.repo = repo;
    }

    /**
     * 当該日付・各タスクインデックスごとの評価を保存または更新します。
     */
    public void saveEvaluations(
            String hrid,
            LocalDate reportDate,
            List<TaskScoreDTO> scores
    ) {
        for (TaskScoreDTO dto : scores) {
            var opt = repo.findByHridAndReportDateAndTaskIndex(
                    hrid, reportDate, dto.taskIndex()
            );
            TaskEvaluation ev = opt.orElseGet(() -> {
                TaskEvaluation e = new TaskEvaluation();
                e.setHrid(hrid);
                e.setReportDate(reportDate);
                e.setTaskIndex(dto.taskIndex());
                return e;
            });
            ev.setScore(dto.score());
            repo.save(ev);
        }
    }

    /**
     * 過去一週間分のタスク評価を取得します。（タスクインデックス昇順）
     */
    public List<TaskEvaluation> findLastWeekEvaluations(String hrid) {
        LocalDate end   = LocalDate.now();
        LocalDate start = end.minusDays(6);
        return repo.findAllByHridAndReportDateBetweenOrderByReportDateAscTaskIndexAsc(
                hrid, start, end
        );
    }

    /** DTO for incoming scores **/
    public static record TaskScoreDTO(int taskIndex, int score) {}
}