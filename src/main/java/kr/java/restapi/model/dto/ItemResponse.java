package kr.java.restapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.java.restapi.model.entity.Item;

import java.time.Instant;

// #(7)-4
@Schema(description = "상품 응답")
public record ItemResponse(

        @Schema(description = "상품 ID", example = "1")
        Long id,

        @Schema(description = "상품명", example = "무선 키보드")
        String name,

        @Schema(description = "가격 (원)", example = "35000")
        Integer price,

        @Schema(description = "상품 설명", example = "블루투스 5.0 지원")
        String description,

        @Schema(description = "생성일시", example = "2024-12-18T10:30:00Z")
        Instant createdAt,

        @Schema(description = "수정일시", example = "2024-12-18T10:30:00Z")
        Instant updatedAt

) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
