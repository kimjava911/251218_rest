package kr.java.restapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

// #(3)-1
// #(7)-5
/**
 * 에러 응답 DTO (모든 API 에러 공통)
 */
@Schema(description = "에러 응답")
public record ErrorResponse(

        @Schema(description = "HTTP 상태 코드", example = "404")
        int status,

        @Schema(description = "에러 메시지", example = "상품을 찾을 수 없습니다. ID: 999")
        String message,

        @Schema(description = "요청 경로", example = "/api/items/999")
        String path,

        @Schema(description = "발생 시각", example = "2024-12-18T10:30:00Z")
        Instant timestamp

) {
    public static ErrorResponse of(int status, String message, String path) {
        return new ErrorResponse(status, message, path, Instant.now());
    }
}
