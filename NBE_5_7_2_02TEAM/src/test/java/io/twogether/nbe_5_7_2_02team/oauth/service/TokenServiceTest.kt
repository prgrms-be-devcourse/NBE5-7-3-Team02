package io.twogether.nbe_5_7_2_02team.oauth.service

import com.github.database.rider.core.api.dataset.DataSet
import io.twogether.nbe_5_7_2_02team.browser.template.BrowserTestTemplate
import io.twogether.nbe_5_7_2_02team.global.annotation.FlywayReset
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenBlackListRepository
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenRepository
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@FlywayReset
@Transactional
internal class TokenServiceTest : BrowserTestTemplate() {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var refreshTokenBlackListRepository: RefreshTokenBlackListRepository

    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    fun refresh_success() {
        val originTokenPair = genTokenPair(1L)

        val refreshTokenPair = tokenService.refreshToken(originTokenPair.refreshToken)

        Assertions.assertEquals(originTokenPair.refreshToken, refreshTokenPair.refreshToken)
    }

    @Test
    @DataSet(value = ["datasets/v2/member.yml"], cleanBefore = true, cleanAfter = true)
    fun logout_success() {
        val tokenPair = genTokenPair(1L)

        tokenService.invalidateRefreshToken(tokenPair.refreshToken)

        val refreshToken = refreshTokenRepository.findByMemberId(1L)

        Assertions.assertNotNull(refreshTokenBlackListRepository.findByRefreshTokenId(refreshToken!!.id))
    }

    private fun genTokenPair(memberId: Long): TokenPair {
        val member = memberRepository.findById(memberId).orElseThrow()
        return jwtTokenProvider.generateTokenPair(member)
    }
}
