package io.twogether.nbe_5_7_2_02team.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "팔로우 요청 DTO")
data class FollowRequest(
    @field:NotNull(message = "잘못된 팔로우 요청입니다.")
    @field:Schema(description = "팔로우 하는 회원 ID", example = "1")
    val followerId: Long,

    @field:NotNull(message = "잘못된 팔로우 요청입니다.")
    @field:Schema(description = "팔로우 받는 회원 ID", example = "2")
    val followingId: Long,
)
