package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

data class LogoutRequest (
    var refreshToken: String
)
