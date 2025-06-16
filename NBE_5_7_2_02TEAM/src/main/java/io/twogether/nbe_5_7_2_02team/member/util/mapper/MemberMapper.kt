package io.twogether.nbe_5_7_2_02team.member.util.mapper

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberCreateResponse

object MemberMapper {
    @JvmStatic
    fun toMemberCreateResponse(member: Member): MemberCreateResponse {
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
