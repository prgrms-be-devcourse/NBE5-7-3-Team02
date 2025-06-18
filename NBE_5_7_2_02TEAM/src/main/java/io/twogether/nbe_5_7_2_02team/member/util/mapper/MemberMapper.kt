package io.twogether.nbe_5_7_2_02team.member.util.mapper

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberCreateResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberUpdateResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MyPageResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MyPageResponse.PostSummary
import io.twogether.nbe_5_7_2_02team.post.domain.Post

fun Member.toMemberCreateResponse(): MemberCreateResponse =
    MemberCreateResponse(
        id = this.id!!,
        email = this.email,
        name = this.name,
        profileImage = this.profileImage,
        job = this.job,
        course = this.course,
        githubId = this.githubId,
    )

fun Member.toMemberUpdateResponse(
    followerCount: Long,
    followingCount: Long,
): MemberUpdateResponse =
    MemberUpdateResponse(
        id = this.id!!,
        name = this.name,
        profileImage = this.profileImage,
        followerCount = followerCount,
        followingCount = followingCount,
        following = false,
        owner = true,
    )

fun Member.toMyPageResponse(
    posts: List<Post>,
    followerCount: Long,
    followingCount: Long,
    following: Boolean,
    owner: Boolean,
): MyPageResponse =
    MyPageResponse(
        id = this.id!!,
        email = this.email,
        name = this.name,
        job = this.job,
        course = this.course,
        profileImage = this.profileImage,
        posts = posts.map { it.toPostSummary() },
        followerCount = followerCount,
        followingCount = followingCount,
        following = following,
        owner = owner,
    )

fun Post.toPostSummary(): PostSummary =
    PostSummary(
        postId = this.id!!,
        title = this.title,
    )