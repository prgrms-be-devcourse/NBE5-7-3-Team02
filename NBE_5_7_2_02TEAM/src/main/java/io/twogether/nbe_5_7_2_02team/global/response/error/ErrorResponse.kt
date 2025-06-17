package io.twogether.nbe_5_7_2_02team.global.response.error

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("code", "message", "errors")
data class ErrorResponse<T>(
    val code: String,
    val message: String,

    // 추가적인 오류 세부 정보를 담기 위한 필드
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val errors: T? = null
)