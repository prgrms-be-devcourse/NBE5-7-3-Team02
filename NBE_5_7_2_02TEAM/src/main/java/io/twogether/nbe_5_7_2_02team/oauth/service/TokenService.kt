package io.twogether.nbe_5_7_2_02team.oauth.service

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshToken
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair
import io.twogether.nbe_5_7_2_02team.oauth.jwt.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class TokenService (
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
){

    // RefreshToken으로 AccessToken과 RefreshToken 재발급
    @Transactional
    open fun refreshToken(refreshTokenValue: String): TokenPair {
        jwtTokenProvider.refreshValidate(refreshTokenValue)

        val tokenBody = jwtTokenProvider.parseJwt(refreshTokenValue)
        val memberId = tokenBody.memberId

        jwtTokenProvider
            .findRefreshToken(memberId)
            .filter { rt: RefreshToken -> rt.refreshToken == refreshTokenValue }
            .orElseThrow { ErrorException(ErrorCode.EXPIRED_REFRESH_TOKEN) }

        val member =
            memberRepository
                .findById(memberId)
                .orElseThrow { ErrorException(ErrorCode.NOT_FOUND_MEMBER) }

        return jwtTokenProvider.generateTokenPair(member)
    }

    // RefreshToken을 무효화해서 로그아웃
    @Transactional
    open fun invalidateRefreshToken(refreshTokenValue: String) {
        jwtTokenProvider.refreshValidate(refreshTokenValue)

        val tokenBody = jwtTokenProvider.parseJwt(refreshTokenValue)
        val memberId = tokenBody.memberId

        val refreshToken =
            jwtTokenProvider
                .findRefreshToken(memberId)
                .filter { rt: RefreshToken -> rt.refreshToken == refreshTokenValue }
                .orElseThrow { ErrorException(ErrorCode.EXPIRED_REFRESH_TOKEN) }

        jwtTokenProvider.addBlackList(refreshToken)
    }
}
