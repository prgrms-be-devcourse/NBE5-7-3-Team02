package io.twogether.nbe_5_7_2_02team.member.util.mapper

import io.twogether.nbe_5_7_2_02team.member.domain.Follow
import io.twogether.nbe_5_7_2_02team.member.dto.request.FollowRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.FollowCreateResponse

fun Long.toFollowRequest(targetId: Long): FollowRequest = FollowRequest(followerId = this, followingId = targetId)

fun Follow.toFollowCreateResponse(
    followerCount: Long,
    followingCount: Long,
): FollowCreateResponse =
    FollowCreateResponse(
        followerId = this.follower.id,
        followingId = this.following.id,
        followerCount = followerCount,
        followingCount = followingCount,
    )
