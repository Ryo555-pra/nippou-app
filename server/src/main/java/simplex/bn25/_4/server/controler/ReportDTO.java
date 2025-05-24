package simplex.bn25._4.server.controler;

// src/main/java/com/example/reportapp/dto/ReportDTO.java



import com.fasterxml.jackson.annotation.JsonFormat;
import simplex.bn25._4.server.model.ReportStatus;

import java.time.LocalDate;

public record ReportDTO(
        /**
         * リクエスト JSON の "reportDate":"yyyy-MM-dd"
         */
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate reportDate,

        /**
         * カリキュラム記入欄のテキスト
         */
        String curriculum,

        /**
         * YWT フォーマット欄のテキスト
         */
        String y,
        String w,
        String t,

        /**
         * ステータス ("NOT_SUBMITTED","SAVED_TEMPORARILY","SUBMITTED")
         */
        ReportStatus status
) {}

