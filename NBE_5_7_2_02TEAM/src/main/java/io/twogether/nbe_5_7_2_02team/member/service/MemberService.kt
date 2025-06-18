package io.twogether.nbe_5_7_2_02team.member.service

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.FollowRepository
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.dto.request.UpdateProfileRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.MemberUpdateResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.MyPageResponse
import io.twogether.nbe_5_7_2_02team.member.util.Uploader.ImageUpload
import io.twogether.nbe_5_7_2_02team.member.util.mapper.toMemberUpdateResponse
import io.twogether.nbe_5_7_2_02team.member.util.mapper.toMyPageResponse
import io.twogether.nbe_5_7_2_02team.post.dao.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
    private val imageUpload: ImageUpload,
) {
    @Transactional(readOnly = true)
    fun getMemberPage(
        targetMemberId: Long,
        viewerId: Long,
    ): MyPageResponse {
        val target = findMember(targetMemberId)
        val posts = postRepository.findAllByMemberId(targetMemberId)

        val followerCount = followRepository.countByFollowing(target)
        val followingCount = followRepository.countByFollower(target)

        // 자기 자신 조회하는 경우에는 true로 고정
        val owner = targetMemberId == viewerId

        // 자기 자신 조회하는 경우에는 false로 고정
        val following =
            if (owner) {
                false
            } else {
                followRepository.existsByFollowerAndFollowing(findMember(viewerId), target)
            }

        return target.toMyPageResponse(
            posts,
            followerCount,
            followingCount,
            following,
            owner,
        )
    }

    @Transactional
    fun updateProfile(
        memberId: Long,
        request: UpdateProfileRequest,
    ): MemberUpdateResponse {
        val member = findMember(memberId)

        val newImageUrl = request.image?.let { imageUpload.saveProfileImage(it, memberId) }
            ?: member.profileImage

        val newNickname = request.nickname.ifBlank { member.name }

        member.updateProfile(newNickname, newImageUrl)


        return member.toMemberUpdateResponse(followRepository.countByFollowing(member)
            , followRepository.countByFollower(member))
    }

    private fun findMember(id: Long): Member =
        memberRepository
            .findById(id)
            .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })
}
