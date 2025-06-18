package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Role

data class SignUpResponse(
    val id: Long,
    val email: String,
    val name: String,
    val job: String,
    val course: String,
    val role: Role
)

