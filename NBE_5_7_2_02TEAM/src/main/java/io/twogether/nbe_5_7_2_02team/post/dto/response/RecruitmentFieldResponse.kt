package io.twogether.nbe_5_7_2_02team.post.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "모집 분야 응답")
data class RecruitmentFieldResponse(
    @field:Schema(description = "모집 분야 이름", example = "Backend")
    val fieldName: String,
    @field:Schema(description = "총 모집 인원", example = "3")
    val totalCount: Int,
    @field:Schema(description = "현재 모집된 인원", example = "1")
    val currentCount: Int,
    @field:Schema(description = "모집 마감 여부", example = "false")
    val closed: Boolean,
)
