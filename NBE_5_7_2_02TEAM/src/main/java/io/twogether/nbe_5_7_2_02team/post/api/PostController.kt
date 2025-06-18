package io.twogether.nbe_5_7_2_02team.post.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Post", description = "게시글 관련 API")
class PostController(
    private val postService: PostService,
) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "게시글 생성",
        description = "게시글을 생성합니다.",
    )
    fun createPost(
        @AuthenticationPrincipal userDetails: UserDetails,
        @ModelAttribute request: @Valid PostCreateRequest,
    ): ResponseEntity<PostResponse> {
        if (StringUtils.isNotBlank(request.recruitmentFieldsJson)) {
            try {
                val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
                request.recruitmentFields =
                    mapper.readValue(
                        request.recruitmentFieldsJson,
                        object : TypeReference<List<RecruitmentFieldRequest>>() {},
                    )
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
    @Operation(
        summary = "게시글 수정",
        description = "기존 게시글의 내용을 수정합니다.",
    )
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
    @Operation(
        summary = "게시글 삭제",
        description = "게시글을 삭제합니다.",
    )
    fun deletePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.deletePost(postId, userDetails.username.toLong())
        return ResponseEntity.ok().build()
    }

    @GetMapping
    @Operation(
        summary = "게시글 목록 조회 (필터 포함)",
        description = "필터 조건에 따라 게시글 목록을 조회합니다.",
    )
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
    @Operation(
        summary = "특정 사용자의 게시글 목록 조회",
        description = "사용자 ID를 기반으로 해당 사용자가 작성한 게시글 목록을 조회합니다.",
    )
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
    @Operation(
        summary = "게시글 상세 조회",
        description = "게시글 ID를 기반으로 상세 정보를 조회합니다.",
    )
    fun getPost(
        @PathVariable postId: Long,
    ): ResponseEntity<PostDetailResponse> {
        val response = postService.getPostById(postId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/{postId}/likes")
    @Operation(
        summary = "게시글 좋아요",
        description = "게시글에 좋아요를 누릅니다.",
    )
    fun likePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.likePost(postId, userDetails.username.toLong())
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{postId}/likes")
    @Operation(
        summary = "게시글 좋아요 취소",
        description = "게시글의 좋아요를 취소합니다.",
    )
    fun unlikePost(
        @PathVariable postId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Void> {
        postService.unlikePost(postId, userDetails.username.toLong())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{postId}/apply")
    @Operation(
        summary = "게시글 모집 분야 지원",
        description = "게시글의 특정 모집 분야에 지원합니다.",
    )
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
