package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import jakarta.validation.constraints.NotBlank

data class GithubLoginRequest(
    @field:NotBlank
    val accessToken: String,
)
