package io.twogether.nbe_5_7_2_02team.member.dto.response

data class MemberCreateResponse(
    val id: Long,
    val email: String,
    val name: String,
    val profileImage: String?,
    val job: String,
    val course: String,
    val githubId: String,
)