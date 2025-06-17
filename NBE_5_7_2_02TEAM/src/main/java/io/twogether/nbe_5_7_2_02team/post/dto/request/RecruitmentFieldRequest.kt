package io.twogether.nbe_5_7_2_02team.post.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class RecruitmentFieldRequest(
    @field:NotBlank
    val fieldName: String,
    @field:Min(1)
    val totalCount: Int,
)
