package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

data class TokenPair (
    val accessToken: String,
    val refreshToken: String
)
