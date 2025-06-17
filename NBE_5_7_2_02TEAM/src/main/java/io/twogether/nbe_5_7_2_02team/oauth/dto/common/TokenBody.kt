package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.twogether.nbe_5_7_2_02team.member.domain.Role

data class TokenBody(
    val memberId: Long,
    val role: Role,
)
