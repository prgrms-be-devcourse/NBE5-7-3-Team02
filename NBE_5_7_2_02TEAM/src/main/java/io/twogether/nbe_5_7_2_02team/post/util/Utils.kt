package io.twogether.nbe_5_7_2_02team.post.util

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.post.dao.PostTagRepository
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import io.twogether.nbe_5_7_2_02team.post.domain.PostTag
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentField
import io.twogether.nbe_5_7_2_02team.post.dto.request.PostCreateRequest
import io.twogether.nbe_5_7_2_02team.post.dto.request.RecruitmentFieldRequest
import io.twogether.nbe_5_7_2_02team.post.dto.response.RecruitmentFieldResponse
import io.twogether.nbe_5_7_2_02team.tag.dao.TagRepository
import io.twogether.nbe_5_7_2_02team.tag.domain.Tag

fun PostCreateRequest.toEntity(member: Member): Post {
    val post = Post(
        title = title,
        content = content,
        recruitmentStatus = recruitmentStatus,
        member = member,
        )

    post.recruitmentDeadline = recruitmentDeadline

    val fields = recruitmentFields.toRecruitmentFields(post)
    post.recruitmentFields.addAll(fields)

    return post
}

fun List<RecruitmentFieldRequest>.toRecruitmentFields (post: Post): List<RecruitmentField> {
    return this.map {
        RecruitmentField(
            post = post,
            fieldName = it.fieldName,
            totalCount = it.totalCount,
            currentCount = 0,
            closed = false
        )
    }
}

fun Post.saveTags(
    tags: List<String>,
    tagRepository: TagRepository,
    postTagRepository: PostTagRepository
) {
    tags.forEach {
        var tag = tagRepository.findByName(it)
        if (tag == null) {
            tag = tagRepository.save(Tag(name = it))
        }
        postTagRepository.save(PostTag(post = this, tag = tag))
    }
}

fun RecruitmentField.toRecruitmentFieldResponse(): RecruitmentFieldResponse {
    return RecruitmentFieldResponse(
        fieldName = fieldName,
        totalCount = totalCount,
        currentCount = currentCount,
        closed = closed
    )
}