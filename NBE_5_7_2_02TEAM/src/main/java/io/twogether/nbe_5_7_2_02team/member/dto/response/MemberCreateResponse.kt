package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Member

data class MemberCreateResponse(
    val id: Long,
    val email: String,
    val name: String,
    val profileImage: String?,
    val job: String,
    val course: String,
    val githubId: String
) {
    companion object {
        @JvmStatic
        fun of(member: Member): MemberCreateResponse {
            return MemberCreateResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                profileImage = member.profileImage,
                job = member.job,
                course = member.course,
                githubId = member.githubId
            )
        }
    }
}
