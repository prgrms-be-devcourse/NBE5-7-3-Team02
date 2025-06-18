package io.twogether.nbe_5_7_2_02team.post.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "게시글 모집 분야 신청 요청")
data class PostApplyRequest(
    @Schema(description = "모집 분야명", example = "Frontend", required = true)
    @JsonProperty("fieldName")
    @field:NotBlank(message = "모집 분야는 필수입니다.")
    val fieldName: String,
)
