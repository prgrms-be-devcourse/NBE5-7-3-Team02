package io.twogether.nbe_5_7_2_02team.member.dto.response

data class FollowCreateResponse(
    val followerId: Long,
    val followingId: Long,
    val followerCount: Long,
    val followingCount: Long,
)
