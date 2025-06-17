package io.twogether.nbe_5_7_2_02team.post.dao

import com.querydsl.core.group.GroupBy
import com.querydsl.core.types.Expression
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import io.twogether.nbe_5_7_2_02team.chat.domain.QChatRoom.chatRoom
import io.twogether.nbe_5_7_2_02team.member.domain.QFollow.follow
import io.twogether.nbe_5_7_2_02team.post.domain.QLikes.likes
import io.twogether.nbe_5_7_2_02team.post.domain.QPost.post
import io.twogether.nbe_5_7_2_02team.post.domain.QPostTag.postTag
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostGetResult
import io.twogether.nbe_5_7_2_02team.post.dto.response.QPostGetResult
import io.twogether.nbe_5_7_2_02team.tag.domain.QTag.tag
import lombok.RequiredArgsConstructor
import org.apache.commons.lang3.BooleanUtils
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class PostRepositoryFilterImpl(
    private val queryFactory: JPAQueryFactory,
) : PostRepositoryFilter {
    override fun findPostsByMemberId(
        memberId: Long,
        lastPostId: Long?,
        limit: Int,
    ): List<PostGetResult> {
        val limitedPostIds =
            queryFactory
                .select(post.id)
                .from(post)
                .where(lastPostIdCondition(lastPostId), post.member.id.eq(memberId))
                .orderBy(post.createdAt.desc())
                .limit(limit.toLong())
                .fetch()

        return getPostResultJoinWithChatRoomAndTag(limitedPostIds, memberId)
    }

    override fun findFilteredPosts(
        memberId: Long?,
        lastPostId: Long?,
        limit: Int,
        recruitmentStatus: RecruitmentStatus,
        isFollowing: Boolean?,
        tags: List<String>,
    ): List<PostGetResult> {
        val limitedPostIds =
            queryFactory
                .select(post.id)
                .from(post)
                .where(
                    recruitmentStatusCondition(recruitmentStatus),
                    followingCondition(memberId, isFollowing),
                    tagsCondition(tags),
                    lastPostIdCondition(lastPostId),
                ).orderBy(post.createdAt.desc())
                .limit(limit.toLong())
                .fetch()

        return getPostResultJoinWithChatRoomAndTag(limitedPostIds, memberId)
    }

    private fun getPostResultJoinWithChatRoomAndTag(
        limitedPostIds: List<Long>,
        memberId: Long?,
    ): List<PostGetResult> =
        queryFactory
            .from(post)
            .leftJoin(post.postTags, postTag)
            .leftJoin(postTag.tag, tag)
            .leftJoin(chatRoom)
            .on(post.id.eq(chatRoom.post.id))
            .where(post.id.`in`(limitedPostIds))
            .orderBy(post.createdAt.desc())
            .transform(
                GroupBy
                    .groupBy(post.id)
                    .list(
                        QPostGetResult(
                            post,
                            likeCount(),
                            chatRoom.id,
                            GroupBy.list(tag.name),
                            isLike(memberId),
                        ),
                    ),
            )

    private fun likeCount(): Expression<Long> =
        ExpressionUtils.`as`(
            JPAExpressions
                .select(likes.count())
                .from(likes)
                .where(likes.post.id.eq(post.id)),
            "likeCount",
        )

    private fun isLike(memberId: Long?): Expression<Boolean> {
        if (memberId == null) {
            return Expressions.FALSE
        }
        return ExpressionUtils.`as`(
            JPAExpressions
                .selectOne()
                .from(likes)
                .where(likes.post.id.eq(post.id), likes.member.id.eq(memberId))
                .exists(),
            "isLike",
        )
    }

    private fun recruitmentStatusCondition(recruitmentStatus: RecruitmentStatus): BooleanExpression? {
        if (recruitmentStatus == RecruitmentStatus.NONE) {
            return null
        }
        return post.recruitmentStatus.eq(recruitmentStatus)
    }

    private fun lastPostIdCondition(lastPostId: Long?): BooleanExpression? {
        if (lastPostId == null) return null

        val lastCreatedAt =
            queryFactory
                .select(post.createdAt)
                .from(post)
                .where(post.id.eq(lastPostId))
                .fetchOne()

        return if (lastCreatedAt != null) post.createdAt.lt(lastCreatedAt) else null
    }

    private fun followingCondition(
        memberId: Long?,
        isFollowing: Boolean?,
    ): BooleanExpression? {
        if (BooleanUtils.isTrue(isFollowing) && memberId != null) {
            return post.member.id.`in`(
                queryFactory
                    .select(follow.following.id)
                    .from(follow)
                    .where(follow.follower.id.eq(memberId)),
            )
        }

        return null
    }

    private fun tagsCondition(tags: List<String>): BooleanExpression? {
        if (tags.isEmpty()) return null
        var condition = Expressions.asBoolean(true).isTrue()
        for (tagName in tags) {
            condition =
                condition.and(
                    JPAExpressions
                        .selectOne()
                        .from(post.postTags, postTag)
                        .join(postTag.tag, tag)
                        .where(
                            postTag.post.id.eq(post.id),
                            tag.name.eq(tagName),
                        ).exists(),
                )
        }
        return condition
    }
}
