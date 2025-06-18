package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "액세스 토큰과 리프레시 토큰 쌍")
data class TokenPair(
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,

    @Schema(description = "리프레시 토큰", example = "dGhpcyBpcyBhIHJlZnJlc2ggdG9rZW4...")
    val refreshToken: String,
)
