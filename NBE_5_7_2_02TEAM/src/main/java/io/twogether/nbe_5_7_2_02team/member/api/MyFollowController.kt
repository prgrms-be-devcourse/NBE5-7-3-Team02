package io.twogether.nbe_5_7_2_02team.member.api

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
class MyFollowController(
    private val followService: FollowService,
) {
    @PostMapping(value = ["/{targetId}"])
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
    fun getFollowersCount(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        val count = followService.getFollowerCount(userDetails.username.toLong())
        return ResponseEntity.ok(count)
    }

    @GetMapping(value = ["/me/followings/count"])
    fun getFollowingsCount(
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        val count = followService.getFollowingCount(userDetails.username.toLong())
        return ResponseEntity.ok(count)
    }
}
