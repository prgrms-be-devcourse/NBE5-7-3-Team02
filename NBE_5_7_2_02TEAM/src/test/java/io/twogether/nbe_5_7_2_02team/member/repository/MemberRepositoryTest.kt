package io.twogether.nbe_5_7_2_02team.member.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class MemberRepositoryTest @Autowired constructor(
    val memberRepository: MemberRepository
) {
    @Test
    fun `이메일로 회원을 조회할 수 있다`() {
        //given
        val member = Member(
            "backend",
            "be_github",
            "backend@test.com",
            "https://example.com/profile.jpg",
            "백엔드 개발자",
             "데브코스",
             Role.MEMBER
        )

        val saved = memberRepository.save(member)

        //when
        val result = memberRepository.findByEmail(saved.email)

        //then
        result shouldNotBe null
        result?.id shouldBe saved.id
        result?.name shouldBe "backend"
        result?.githubId shouldBe "be_github"
        result?.email shouldBe "be_github"
        result?.profileImage shouldBe "https://example.com/profile.jpg"
        result?.job shouldBe "백엔드 개발자"
        result?.course shouldBe "데브코스"
    }
}