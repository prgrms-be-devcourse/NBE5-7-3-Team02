package io.twogether.nbe_5_7_2_02team.member.api

import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberCreateResponse
import io.twogether.nbe_5_7_2_02team.member.service.FollowService
import lombok.RequiredArgsConstructor
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
class PublicFollowController(
    private val followService: FollowService
) {

    @GetMapping(value = ["/{memberId}/followers"])
    fun getFollowers(
        @PathVariable memberId: Long,
        @PageableDefault(
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<MemberCreateResponse>> {
        val followers = followService.getFollowers(memberId, pageable)
        return ResponseEntity.ok(followers)
    }

    @GetMapping("/{memberId}/followings")
    fun getFollowings(
        @PathVariable memberId: Long,
        @PageableDefault(
            size = 20,
            sort = ["id"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<MemberCreateResponse>> {
        val followings = followService.getFollowings(memberId, pageable)
        return ResponseEntity.ok(followings)
    }

    @GetMapping("/{memberId}/followers/count")
    fun getFollwersCount(@PathVariable memberId: Long): ResponseEntity<Long> {
        val count = followService.getFollowerCount(memberId)
        return ResponseEntity.ok(count)
    }

    @GetMapping("/{memberId}/followings/count")
    fun getFollwingsCount(@PathVariable memberId: Long): ResponseEntity<Long> {
        val count = followService.getFollowingCount(memberId)
        return ResponseEntity.ok(count)
    }
}
