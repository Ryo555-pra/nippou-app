package simplex.bn25._4.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import simplex.bn25._4.server.model.ReportStatus;

import java.time.LocalDate;

/**
 * 日報のリクエスト／レスポンス用 DTO
 */
public record ReportDTO(
        /** 日付 ("yyyy-MM-dd" 形式) */
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reportDate,

        /** カリキュラム記入欄 */
        String curriculum,

        /** Y: やったこと */
        String y,

        /** W: わかったこと */
        String w,

        /** T: 次にすること */
        String t,

        /** ステータス */
        ReportStatus status
) {}
