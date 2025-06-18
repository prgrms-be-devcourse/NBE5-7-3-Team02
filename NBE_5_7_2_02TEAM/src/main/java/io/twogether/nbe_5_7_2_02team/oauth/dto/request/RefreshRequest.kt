package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "액세스 토큰 재발급 요청 DTO")
data class RefreshRequest(
    @field:NotBlank
    @Schema(
        description = "재발급에 사용할 리프레시 토큰",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    )
    val refreshToken: String,
)
