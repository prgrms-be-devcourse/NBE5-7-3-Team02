package io.twogether.nbe_5_7_2_02team.post.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class PostApplyRequest (
    @JsonProperty("fieldName")
    @field:NotBlank(message = "모집 분야는 필수입니다.")
    val fieldName: String
)
