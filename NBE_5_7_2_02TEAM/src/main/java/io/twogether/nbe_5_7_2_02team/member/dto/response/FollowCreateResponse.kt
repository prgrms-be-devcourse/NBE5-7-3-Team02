package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "팔로우 생성 응답 DTO")
data class FollowCreateResponse(
    @field:Schema(description = "팔로워 ID", example = "1")
    val followerId: Long,

    @field:Schema(description = "팔로잉 ID", example = "2")
    val followingId: Long,

    @field:Schema(description = "팔로워 수", example = "100")
    val followerCount: Long,

    @field:Schema(description = "팔로잉 수", example = "50")
    val followingCount: Long,
)
