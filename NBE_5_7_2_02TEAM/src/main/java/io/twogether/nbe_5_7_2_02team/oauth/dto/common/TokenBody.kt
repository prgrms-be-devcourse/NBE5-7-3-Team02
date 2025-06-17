package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.twogether.nbe_5_7_2_02team.member.domain.Role
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

data class TokenBody (
    val memberId: Long,
    val role: Role
)
