package io.twogether.nbe_5_7_2_02team.post.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Schema(description = "모집 분야 요청 정보")
data class RecruitmentFieldRequest(
    @field:NotBlank
    @Schema(description = "모집 분야명", example = "Backend", required = true)
    val fieldName: String,
    @field:Min(1)
    @Schema(description = "모집 인원 수", example = "3", minimum = "1", required = true)
    val totalCount: Int,
)
