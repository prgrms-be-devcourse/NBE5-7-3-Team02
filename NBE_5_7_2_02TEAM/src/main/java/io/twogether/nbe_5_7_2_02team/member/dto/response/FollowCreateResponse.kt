package io.twogether.nbe_5_7_2_02team.member.dto.response

import io.twogether.nbe_5_7_2_02team.member.domain.Follow
import lombok.AccessLevel
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

data class FollowCreateResponse(
    val followerId: Long,
    val followingId: Long,
    val followerCount: Long,
    val followingCount: Long

)
