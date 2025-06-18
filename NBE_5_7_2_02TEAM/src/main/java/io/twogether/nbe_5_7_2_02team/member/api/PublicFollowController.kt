package io.twogether.nbe_5_7_2_02team.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberCreateResponse
import io.twogether.nbe_5_7_2_02team.member.service.FollowService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/follow/public")
@Tag(name = "Public Follow", description = "공개 팔로우 조회 API")
class PublicFollowController(
    private val followService: FollowService,
) {
    @GetMapping(value = ["/{memberId}/followers"])
    @Operation(
        summary = "사용자의 팔로워 목록 조회",
        description = "특정 사용자를 팔로우하는 사람들의 목록을 페이징하여 조회합니다.",
    )
    fun getFollowers(
        @PathVariable memberId: Long,
        @PageableDefault(
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
    ): ResponseEntity<Page<MemberCreateResponse>> {
        val followers = followService.getFollowers(memberId, pageable)
        return ResponseEntity.ok(followers)
    }

    @GetMapping("/{memberId}/followings")
    @Operation(
        summary = "사용자의 팔로잉 목록 조회",
        description = "특정 사용자가 팔로우한 사람들의 목록을 페이징하여 조회합니다.",
    )
    fun getFollowings(
        @PathVariable memberId: Long,
        @PageableDefault(
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC,
        ) pageable: Pageable,
    ): ResponseEntity<Page<MemberCreateResponse>> {
        val followings = followService.getFollowings(memberId, pageable)
        return ResponseEntity.ok(followings)
    }

    @GetMapping("/{memberId}/followers/count")
    @Operation(
        summary = "사용자의 팔로워 수 조회",
        description = "특정 사용자의 팔로워 수를 조회합니다.",
    )
    fun getFollowersCount(
        @PathVariable memberId: Long,
    ): ResponseEntity<Long> {
        val count = followService.getFollowerCount(memberId)
        return ResponseEntity.ok(count)
    }

    @GetMapping("/{memberId}/followings/count")
    @Operation(
        summary = "사용자의 팔로잉 수 조회",
        description = "특정 사용자가 팔로우한 사람들의 수를 조회합니다.",
    )
    fun getFollowingsCount(
        @PathVariable memberId: Long,
    ): ResponseEntity<Long> {
        val count = followService.getFollowingCount(memberId)
        return ResponseEntity.ok(count)
    }
}
