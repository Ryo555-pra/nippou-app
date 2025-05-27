package simplex.bn25._4.server.controler;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskScoreDTO(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportDate,
        int taskIndex,
        int score
) {}

