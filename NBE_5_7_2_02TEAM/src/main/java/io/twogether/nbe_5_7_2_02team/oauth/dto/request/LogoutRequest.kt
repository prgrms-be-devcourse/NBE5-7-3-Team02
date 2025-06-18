package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "로그아웃 요청 DTO")
data class LogoutRequest(
    @field:NotBlank
    @Schema(
        description = "재발급용 리프레시 토큰",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    var refreshToken: String,
)
