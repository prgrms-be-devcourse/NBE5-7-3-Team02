package io.twogether.nbe_5_7_2_02team.member.dto.response

import lombok.AccessLevel
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

data class MemberCreateResponse(
    val id: Long,
    val email: String,
    val name: String,
    val profileImage: String?,
    val job: String,
    val course: String,
    val githubId: String
)
