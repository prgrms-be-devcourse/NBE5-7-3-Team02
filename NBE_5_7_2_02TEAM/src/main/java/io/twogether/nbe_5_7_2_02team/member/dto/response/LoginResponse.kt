package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Role
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair

data class LoginResponse(
    val tokenPair: TokenPair,
    val role: Role,
    val memberId: Long,
)
