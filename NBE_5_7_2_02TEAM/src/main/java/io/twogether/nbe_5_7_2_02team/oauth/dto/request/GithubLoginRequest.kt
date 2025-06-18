package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "GitHub 로그인 요청 DTO")
data class GithubLoginRequest(
    @field:NotBlank
    @Schema(description = "GitHub에서 발급받은 액세스 토큰", example = "gho_123abc456def789ghi")
    val accessToken: String,
)
