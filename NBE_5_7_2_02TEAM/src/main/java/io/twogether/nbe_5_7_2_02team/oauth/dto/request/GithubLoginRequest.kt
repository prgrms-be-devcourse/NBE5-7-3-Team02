package io.twogether.nbe_5_7_2_02team.oauth.dto.request

import lombok.Getter
import lombok.NoArgsConstructor

data class GithubLoginRequest (
    val accessToken: String
)