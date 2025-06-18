package io.twogether.nbe_5_7_2_02team.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.twogether.nbe_5_7_2_02team.member.dto.response.FollowCreateResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberCreateResponse
import io.twogether.nbe_5_7_2_02team.member.service.FollowService
import io.twogether.nbe_5_7_2_02team.member.util.mapper.toFollowRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/follow")
@Tag(name = "Follow", description = "팔로우/언팔로우 및 팔로우 정보 API")
class MyFollowController(
    private val followService: FollowService,
) {
    @PostMapping(value = ["/{targetId}"])
    @Operation(
        summary = "팔로우 하기",
        description = "특정 사용자를 팔로우합니다.",
    )
    fun follow(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable targetId: Long,
    ): ResponseEntity<FollowCreateResponse> {
        val followRequest =
            userDetails.username.toLong().toFollowRequest(targetId)
        val response = followService.createFollow(followRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping(value = ["/{targetId}"])
    @Operation(
        summary = "언팔로우 하기",
        description = "특정 사용자를 언팔로우합니다.",
    )
    fun unfollow(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable targetId: Long,
    ): ResponseEntity<Void> {
        followService.deleteFollow(
            userDetails.username.toLong().toFollowRequest(targetId),
        )
        return ResponseEntity.ok().build()
    }

    @GetMapping(value = ["/me/followers"])
    @Operation(
        summary = "내 팔로워 목록 조회",
        description = "현재 로그인한 사용자를 팔로우하는 사용자 목록을 페이징하여 조회합니다.",
    )
    fun getFollowers(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PageableDefault(
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
    ): ResponseEntity<Page<MemberCreateResponse>> {
        val followers =
            followService
                .getFollowers(userDetails.username.toLong(), pageable)
        return ResponseEntity.ok(followers)
    }

    @GetMapping(value = ["/me/followings"])
    @Operation(
        summary = "내 팔로잉 목록 조회",
        description = "현재 로그인한 사용자가 팔로우한 사용자 목록을 페이징하여 조회합니다.",
    )
    fun getFollowings(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PageableDefault(
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
    ): ResponseEntity<Page<MemberCreateResponse>> {
        val followings =
            followService.getFollowings(userDetails.username.toLong(), pageable)
        return ResponseEntity.ok(followings)
    }

    @GetMapping(value = ["/me/followers/count"])
    @Operation(
        summary = "내 팔로워 수 조회",
        description = "현재 로그인한 사용자의 팔로워 수를 조회합니다.",
    )
    fun getFollowersCount(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        val count = followService.getFollowerCount(userDetails.username.toLong())
        return ResponseEntity.ok(count)
    }

    @GetMapping(value = ["/me/followings/count"])
    @Operation(
        summary = "내 팔로잉 수 조회",
        description = "현재 로그인한 사용자가 팔로우한 사용자 수를 조회합니다.",
    )
    fun getFollowingsCount(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        val count = followService.getFollowingCount(userDetails.username.toLong())
        return ResponseEntity.ok(count)
    }
}
