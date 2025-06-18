package io.twogether.nbe_5_7_2_02team.member.dto.request

import jakarta.validation.constraints.NotNull

data class FollowRequest(
    @field:NotNull(message = "잘못된 팔로우 요청입니다.")
    val followerId: Long,
    @field:NotNull(message = "잘못된 팔로우 요청입니다.")
    val followingId: Long,
)
