package io.twogether.nbe_5_7_2_02team.post.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.twogether.nbe_5_7_2_02team.post.dto.request.*
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostDetailResponse
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostGetResponse
import io.twogether.nbe_5_7_2_02team.post.dto.response.PostResponse
import io.twogether.nbe_5_7_2_02team.post.service.PostService
import jakarta.validation.Valid
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
    private val mapper: ObjectMapper,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createPost(
        @AuthenticationPrincipal userDetails: UserDetails,
        @ModelAttribute request: @Valid PostCreateRequest,
    ): ResponseEntity<PostResponse> {
        if (StringUtils.isNotBlank(request.recruitmentFieldsJson)) {
            try {
                request.recruitmentFields = mapper.readValue(request.recruitmentFieldsJson,
                    object : TypeReference<List<RecruitmentFieldRequest>>() {})
            } catch (e: Exception) {
                // TODO: 커스텀 Exception으로 변경
                throw RuntimeException("Invalid recruitmentFieldsJson", e)
            }
        }

        val response =
            postService.createPost(request, userDetails.username.toLong())

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PatchMapping(value = ["/{postId}"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updatePost(
        @PathVariable postId: Long,
        @RequestPart("post") request: PostUpdateRequest,
        @RequestPart(value = "images", required = false) images: List<MultipartFile> = listOf(),
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<PostResponse> {
        request.images = images
        val response =
            postService.updatePost(postId, request, userDetails.username.toLong())
        return ResponseEntity.ok(response)
    }

    @DeleteMapping(value = ["/{postId}"])
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.deletePost(postId, userDetails.username.toLong())
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun findFilteredPosts(
        @ModelAttribute request: PostGetRequest,
        @AuthenticationPrincipal userDetails: UserDetails?,
    ): ResponseEntity<PostGetResponse> {
        val response = postService.getFilteredPosts(request, userDetails)
        if (CollectionUtils.isEmpty(response.posts)) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/member/{memberId}")
    fun findPosts(
        @ModelAttribute request: PostGetRequest,
        @PathVariable memberId: Long,
    ): ResponseEntity<PostGetResponse> {
        val response = postService.getPostsByMember(request, memberId)
        if (CollectionUtils.isEmpty(response.posts)) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{postId}")
    fun getPost(
        @PathVariable postId: Long,
    ): ResponseEntity<PostDetailResponse> {
        val response = postService.getPostById(postId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{postId}/likes")
    fun likePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.likePost(postId, userDetails.username.toLong())
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{postId}/likes")
    fun unlikePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.unlikePost(postId, userDetails.username.toLong())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{postId}/apply")
    fun applyToField(
        @PathVariable postId: Long,
        @RequestBody request: @Valid PostApplyRequest,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.apply(
            postId,
            request.fieldName,
            userDetails.username.toLong(),
        )
        return ResponseEntity.ok().build()
    }
}
