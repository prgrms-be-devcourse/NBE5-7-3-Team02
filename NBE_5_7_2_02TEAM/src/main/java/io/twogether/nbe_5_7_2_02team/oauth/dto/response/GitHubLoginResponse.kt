package io.twogether.nbe_5_7_2_02team.oauth.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GitHub 로그인 응답 DTO")
data class GitHubLoginResponse(
    @Schema(
        description = "발급된 액세스 토큰 (JWT)",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    val accessToken: String,
)
