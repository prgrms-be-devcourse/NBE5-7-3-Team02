package io.twogether.nbe_5_7_2_02team.post.service

import io.twogether.nbe_5_7_2_02team.chat.dao.ChatRepository
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.post.dao.LikesRepository
import io.twogether.nbe_5_7_2_02team.post.dao.PostApplicationRepository
import io.twogether.nbe_5_7_2_02team.post.dao.PostRepository
import io.twogether.nbe_5_7_2_02team.post.dao.PostTagRepository
import io.twogether.nbe_5_7_2_02team.post.domain.*
import io.twogether.nbe_5_7_2_02team.post.dto.request.PostCreateRequest
import io.twogether.nbe_5_7_2_02team.post.dto.request.PostGetRequest
import io.twogether.nbe_5_7_2_02team.post.dto.request.PostUpdateRequest
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostDetailResponse
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostGetResponse
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostResponse
import io.twogether.nbe_5_7_2_02team.post.util.ImageUploader
import io.twogether.nbe_5_7_2_02team.post.util.PostMapper
import io.twogether.nbe_5_7_2_02team.tag.dao.TagRepository
import org.apache.commons.collections4.CollectionUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postTagRepository: PostTagRepository,
    private val memberRepository: MemberRepository,
    private val postMapper: PostMapper,
    private val imageUploader: ImageUploader,
    private val chatRepository: ChatRepository,
    private val likesRepository: LikesRepository,
    private val postApplicationRepository: PostApplicationRepository,
    private val tagRepository: TagRepository,
) {
    @Transactional
    fun createPost(request: PostCreateRequest, memberId: Long): PostResponse {
        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })

        val post = postMapper.toEntity(request, member)
        postRepository.save(post)

        if (CollectionUtils.isNotEmpty(request.images)) {
            val savedPaths = imageUploader.saveImages(request.images, post.id)
            post.imageUrls = savedPaths
        }

        val postTags = postMapper.toPostTags(post, request.tags)
        postTagRepository.saveAll(postTags)

        return PostResponse(post.id)
    }

    @Transactional
    fun updatePost(postId: Long, request: PostUpdateRequest, memberId: Long): PostResponse {
        val updatePost =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        if (updatePost.member.id != memberId) {
            throw ErrorException(ErrorCode.UNAUTHORIZED_POST_ACCESS)
        }

        postMapper.updateFromRequest(updatePost, request)

        if (CollectionUtils.isNotEmpty(request.images)) {
            imageUploader.deletePostImageByFolder(postId)
            val savedPaths = imageUploader.saveImages(request.images, postId)
            updatePost.imageUrls = savedPaths
        }

        postTagRepository.deleteAllByPost(updatePost)
        if (CollectionUtils.isNotEmpty(request.tags)) {
            val newTags = postMapper.toPostTags(updatePost, request.tags)
            postTagRepository.saveAll(newTags)
        }
        deleteUnusedTags()

        if (CollectionUtils.isNotEmpty(request.recruitmentFields)) {
            updatePost.recruitmentFields.clear()
            val newFields =
                postMapper.toRecruitmentFields(updatePost, request.recruitmentFields)
            updatePost.recruitmentFields.addAll(newFields)
        }

        return PostResponse(updatePost.id)
    }

    @Transactional
    fun deletePost(postId: Long, memberId: Long) {
        val deletePost =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        if (deletePost.member.id != memberId) {
            throw ErrorException(ErrorCode.UNAUTHORIZED_POST_ACCESS)
        }

        likesRepository.deleteByPost(deletePost)
        chatRepository.deleteByPost(deletePost)
        imageUploader.deletePostImageByFolder(deletePost.id)
        postRepository.delete(deletePost)
        deleteUnusedTags()
    }

    private fun deleteUnusedTags() {
        val referredTagIds: List<Long> = postTagRepository.findAllTagIds()
        tagRepository.deleteUnusedTags(referredTagIds)
    }

    @Transactional(readOnly = true)
    fun getFilteredPosts(request: PostGetRequest, userDetails: UserDetails?): PostGetResponse {
        val memberId = userDetails?.username?.toLong()

        return PostGetResponse.from(
            postRepository.findFilteredPosts(
                memberId,
                request.lastPostId,
                request.limit,
                parseRecruitmentStatus(request.isRecruit),
                request.isFollowing,
                request.tags
            )
        )
    }

    @Transactional(readOnly = true)
    fun getPostsByMember(request: PostGetRequest, memberId: Long?): PostGetResponse {
        return PostGetResponse.from(
            postRepository.findPostsByMemberId(
                memberId, request.lastPostId, request.limit
            )
        )
    }

    private fun parseRecruitmentStatus(isRecruit: Boolean?): RecruitmentStatus {
        if (isRecruit != null) {
            return if (isRecruit) RecruitmentStatus.RECRUITING else RecruitmentStatus.DONE
        }
        return RecruitmentStatus.NONE
    }

    @Transactional(readOnly = true)
    fun getPostById(postId: Long): PostDetailResponse {
        val post =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        val tagNames =
            post.postTags.map { postTag: PostTag -> postTag.tag.name }
                .toList()

        return PostDetailResponse(
            post.title,
            post.content,
            post.recruitmentStatus,
            tagNames,
            post.imageUrls
        )
    }

    @Transactional
    fun likePost(postId: Long, memberId: Long) {
        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })

        val post =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        val existingLike: Boolean = likesRepository.existsByPostAndMember(post, member)
        if (existingLike) {
            throw ErrorException(ErrorCode.LIKE_ALREADY_EXIST)
        }

        val likes = Likes(member, post)
        likesRepository.save(likes)
    }

    @Transactional
    fun unlikePost(postId: Long, memberId: Long) {
        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })

        val post =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        val likes: Likes = likesRepository.findByPostAndMember(post, member) ?: throw ErrorException(ErrorCode.NOT_FOUND_LIKE)
        likesRepository.delete(likes)
    }

    @Transactional
    fun apply(postId: Long, fieldName: String, memberId: Long) {
        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })

        val post =
            postRepository
                .findById(postId)
                .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_POST) })

        if (post.recruitmentStatus != RecruitmentStatus.RECRUITING) {
            throw ErrorException(ErrorCode.RECRUITMENT_NOT_AVAILABLE)
        }

        if (post.member.id == memberId) {
            throw ErrorException(ErrorCode.CANNOT_APPLY_TO_OWN_POST)
        }

        val field =
            post.recruitmentFields
                .stream()
                .filter { f: RecruitmentField ->
                    f.fieldName.trim()
                        .equals(fieldName.trim(), ignoreCase = true)
                }
                .findFirst()
                .orElseThrow(
                    Supplier { ErrorException(ErrorCode.NOT_FOUND_RECRUITMENT_FIELD) })

        if (field.isClosed) {
            throw ErrorException(ErrorCode.RECRUITMENT_CLOSED)
        }

        val alreadyApplied = postApplicationRepository.existsByMemberAndField(member, field)
        if (alreadyApplied) {
            throw ErrorException(ErrorCode.ALREADY_APPLIED)
        }

        val application = PostApplication(member, post, field)
        postApplicationRepository.save(application)

        field.increaseCount()
    }
}
