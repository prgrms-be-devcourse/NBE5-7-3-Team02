package io.twogether.nbe_5_7_2_02team.post.dao

import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostGetResult

interface PostRepositoryFilter {
    fun findPostsByMemberId(
        memberId: Long,
        lastPostId: Long?,
        limit: Int
    ): List<PostGetResult>

    fun findFilteredPosts(
        memberId: Long?,
        lastPostId: Long?,
        limit: Int,
        recruitmentStatus: RecruitmentStatus,
        isFollowing: Boolean?,
        tags: List<String>
    ): List<PostGetResult>
}
