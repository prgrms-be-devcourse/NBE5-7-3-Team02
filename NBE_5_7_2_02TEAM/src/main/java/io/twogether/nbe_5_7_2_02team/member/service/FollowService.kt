package io.twogether.nbe_5_7_2_02team.member.service

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.FollowRepository
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.domain.Follow
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.dto.request.FollowRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.FollowCreateResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberCreateResponse
import io.twogether.nbe_5_7_2_02team.member.util.mapper.toFollowCreateResponse
import io.twogether.nbe_5_7_2_02team.member.util.mapper.toMemberCreateResponse
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
@RequiredArgsConstructor
class FollowService(
    private val followRepository: FollowRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun createFollow(
        followRequest: @Valid FollowRequest
    ): FollowCreateResponse {
        val follower = findMemberOrThrow(followRequest.followerId, ErrorCode.NOT_FOUND_FOLLOWER)
        val following = findMemberOrThrow(followRequest.followingId, ErrorCode.NOT_FOUND_FOLLOWING)

        if (follower == following) {
            throw ErrorException(ErrorCode.NOT_YOURSELF_FOLLOW)
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw ErrorException(ErrorCode.NOT_DUPLICATION_FOLLOW)
        }

        val follow = followRepository.save(Follow(follower, following))
        val followerCount = followRepository.countByFollowing(following)
        val followingCount = followRepository.countByFollower(follower)

        return follow.toFollowCreateResponse(followerCount, followingCount)
    }

    @Transactional
    fun deleteFollow(followRequest: FollowRequest) {
        val follower = findMemberOrThrow(followRequest.followerId, ErrorCode.NOT_FOUND_FOLLOWER)
        val following = findMemberOrThrow(followRequest.followingId, ErrorCode.NOT_FOUND_FOLLOWING)

        followRepository.deleteByFollowerAndFollowing(follower, following)
    }

    @Transactional(readOnly = true)
    fun getFollowerCount(memberId: Long): Long {
        val member = findMember(memberId)
        return followRepository.countByFollowing(member)
    }

    @Transactional(readOnly = true)
    fun getFollowingCount(memberId: Long): Long {
        val member = findMember(memberId)
        return followRepository.countByFollower(member)
    }

    @Transactional(readOnly = true)
    fun getFollowers(memberId: Long, pageable: Pageable): Page<MemberCreateResponse> {
        val member = findMember(memberId)
        return followRepository
            .findFollowerMembers(member, pageable)
            .map { it.toMemberCreateResponse() }
    }

    @Transactional(readOnly = true)
    fun getFollowings(memberId: Long, pageable: Pageable): Page<MemberCreateResponse> {
        val member = findMember(memberId)
        return followRepository
            .findFollowingMembers(member, pageable)
            .map { it.toMemberCreateResponse() }
    }

    private fun findMember(memberId: Long): Member {
        return memberRepository.findById(memberId)
            .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })
    }

    private fun findMemberOrThrow(id: Long, errorCode: ErrorCode): Member =
        memberRepository.findById(id).orElseThrow { ErrorException(errorCode) }
}
