package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import io.twogether.nbe_5_7_2_02team.member.dto.response.SignUpResponse

data class SignUpResponse(
    val id: Long,
    val email: String,
    val name: String,
    val job: String,
    val course: String,
    val role: Role

) {

    companion object {
        @JvmStatic
        fun from(member: Member): SignUpResponse =
            SignUpResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                job = member.job,
                course = member.course,
                role = member.role
            )

    }
}
