package io.twogether.nbe_5_7_2_02team.member.dto.response

data class MemberUpdateResponse(
    val id: Long,
    val name: String,
    val profileImage: String?,
    val followerCount: Long,
    val followingCount: Long,
    val following: Boolean,
    val owner: Boolean,
)
