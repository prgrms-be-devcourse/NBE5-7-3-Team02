package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import jakarta.validation.constraints.NotBlank

data class LogoutRequest(
    @field:NotBlank
    var refreshToken: String,
)
