package simplex.bn25._4.server.controler;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReportSummaryDTO(
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate reportDate,
        String curriculum,
        String t
) {}
