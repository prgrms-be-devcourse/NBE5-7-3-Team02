package io.twogether.nbe_5_7_2_02team.post.dto.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.querydsl.core.annotations.QueryProjection
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import io.twogether.nbe_5_7_2_02team.post.util.toRecruitmentFieldResponse

data class PostGetResult
@QueryProjection constructor(
    @JsonIgnore
    val post: Post,
    val numLikes: Long,
    val chatRoomId: Long,
    val tags: List<String>,
    val isLike: Boolean
) {
    val postId = post.id!!
    val title = post.title
    val content = post.content
    val memberId = post.member.id
    val memberName = post.member.name
    val memberImage = post.member.profileImage
    val createdAt = post.createdAt
    val updatedAt = post.updatedAt
    val recruitmentStatus = post.recruitmentStatus.name
    val recruitmentDeadline = post.recruitmentDeadline
    val recruitmentFields = post.recruitmentFields.map { r -> r.toRecruitmentFieldResponse() }
    val images = post.imageUrls
}
