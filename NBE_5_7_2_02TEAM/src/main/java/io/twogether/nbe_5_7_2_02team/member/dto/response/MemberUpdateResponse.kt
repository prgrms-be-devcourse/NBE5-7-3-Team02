package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Member

data class MemberUpdateResponse(
    val id: Long?,
    val name: String,
    val profileImage: String?,
    val followerCount: Long,
    val followingCount: Long,

) {

    companion object {
        @JvmStatic
        fun of(
            member: Member,
            followerCount: Long,
            followingCount: Long,
        ): MemberUpdateResponse =
            MemberUpdateResponse(
                id = member.id!!,
                name = member.name,
                profileImage = member.profileImage,
                followerCount = followerCount,
                followingCount = followingCount,
            )


    }


}
