package io.twogether.nbe_5_7_2_02team.member

import io.twogether.nbe_5_7_2_02team.global.annotation.FlywayReset
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import io.twogether.nbe_5_7_2_02team.member.dto.request.FollowRequest
import io.twogether.nbe_5_7_2_02team.member.service.FollowService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@FlywayReset
@SpringBootTest
@Transactional
class FollowServiceTest
@Autowired
constructor(
    private val followService: FollowService,
    private val memberRepository: MemberRepository,
) {
    private lateinit var follower1: Member
    private lateinit var follower2: Member
    private lateinit var following1: Member
    private lateinit var following2: Member

    @BeforeEach
    fun setUp() {
        follower1 =
            memberRepository.save(
                Member(
                    "ghFollower@example.com",
                    "홍길동",
                    "follower.png",
                    "Backend",
                    "3기",
                    "ghFollower",
                    Role.MEMBER,
                ),
            )

        follower2 =
            memberRepository.save(
                Member(
                    "ghFollower2@example.com",
                    "홍길동2",
                    "follower2.png",
                    "Backend",
                    "3기",
                    "ghFollower2",
                    Role.MEMBER,
                ),
            )

        following1 =
            memberRepository.save(
                Member(
                    "ghFollowing@example.com",
                    "임꺽정",
                    "following.png",
                    "Frontend",
                    "2기",
                    "ghFollowing",
                    Role.MEMBER,
                ),
            )

        following2 =
            memberRepository.save(
                Member(
                    "ghFollowing2@example.com",
                    "임꺽정2",
                    "following2.png",
                    "Frontend",
                    "2기",
                    "ghFollowing2",
                    Role.MEMBER,
                ),
            )
    }

    @Test
    @DisplayName("팔로우 생성 성공")
    fun `팔로우 생성 성공`() {
        val request = FollowRequest(follower1.id!!, following1.id!!)
        val response = followService.createFollow(request)

        Assertions.assertThat(response.followerId).isEqualTo(follower1.getId())
        Assertions.assertThat(response.followingId).isEqualTo(following1.getId())
    }

    @Test
    @DisplayName("자기 자신은 팔로우할 수 없다")
    fun `자기 자신은 팔로우할 수 없다`() {
        val request = FollowRequest(follower1.id!!, follower1.id!!)

        val ex = assertThrows<ErrorException> {
            followService.createFollow(request)
        }

        Assertions.assertThat(ex.errorCode).isEqualTo(ErrorCode.NOT_YOURSELF_FOLLOW)
    }

    @Test
    @DisplayName("중복 팔로우는 불가능하다")
    fun `중복 팔로우는 불가능하다`() {
        val request = FollowRequest(follower1.id!!, following1.id!!)
        followService.createFollow(request)

        val ex = assertThrows<ErrorException> {
            followService.createFollow(request)
        }

        Assertions.assertThat(ex.errorCode).isEqualTo(ErrorCode.NOT_DUPLICATION_FOLLOW)
    }

    @Test
    @DisplayName("팔로우 삭제 성공")
    fun `팔로우 삭제 성공`() {
        val request = FollowRequest(follower1.id!!, following1.id!!)

        followService.createFollow(request)
        followService.deleteFollow(request)
    }

    @Test
    @DisplayName("팔로워 수 조회")
    fun `팔로워 수 조회`() {
        followService.createFollow(FollowRequest(follower1.id!!, following1.id!!))
        followService.createFollow(FollowRequest(follower2.id!!, following1.id!!))

        val count = followService.getFollowerCount(following1.id)

        Assertions.assertThat(count).isEqualTo(2L)
    }

    @Test
    @DisplayName("팔로잉 수 조회")
    fun `팔로잉 수 조회`() {
        followService.createFollow(FollowRequest(follower1.id!!, following1.id!!))
        followService.createFollow(FollowRequest(follower1.id!!, following2.id!!))

        val count = followService.getFollowingCount(follower1.id!!)

        Assertions.assertThat(count).isEqualTo(2L)
    }

    @Test
    @DisplayName("팔로워 목록 조회")
    fun `팔로워 목록 조회`() {
        followService.createFollow(FollowRequest(follower1.id!!, following1.id!!))
        followService.createFollow(FollowRequest(follower2.id!!, following1.id!!))

        val pages = followService.getFollowers(following1.id!!, PageRequest.of(0, 5))

        Assertions.assertThat(pages).hasSize(2)
        Assertions
            .assertThat(pages)
            .extracting<String> { it.githubId }
            .containsExactlyInAnyOrder("ghFollower", "ghFollower2")
    }

    @Test
    @DisplayName("팔로잉 목록 조회")
    fun `팔로잉 목록 조회`() {
        followService.createFollow(FollowRequest(follower1.id!!, following1.id!!))
        followService.createFollow(FollowRequest(follower1.id!!, following2.id!!))

        val pages = followService.getFollowings(follower1.id!!, PageRequest.of(0, 5))

        Assertions.assertThat(pages).hasSize(2)
        Assertions
            .assertThat(pages)
            .extracting<String> { it.githubId }
            .containsExactlyInAnyOrder("ghFollowing", "ghFollowing2")
    }
}
