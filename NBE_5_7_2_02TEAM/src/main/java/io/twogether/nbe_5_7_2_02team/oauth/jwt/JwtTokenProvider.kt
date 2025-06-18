package io.twogether.nbe_5_7_2_02team.oauth.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenBlackListRepository
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenRepository
import io.twogether.nbe_5_7_2_02team.oauth.dao.TokenRepository
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshToken
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenBody
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenPair
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.crypto.SecretKey

@Service
@Transactional
class JwtTokenProvider(
    private val jwtConfiguration: JwtConfiguration,
    private val tokenRepository: TokenRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val refreshTokenBlackListRepository: RefreshTokenBlackListRepository,
) {
    fun generateTokenPair(member: Member): TokenPair {
        val token = refreshTokenRepository.findByMemberId(member.id!!)
        if (token != null) {
            refreshTokenBlackListRepository.deleteByRefreshToken(token)
            refreshTokenRepository.delete(token)
        }

        val accessToken = issueAccessToken(member.id, member.role)
        val refreshToken = issueRefreshToken(member.id, member.role)

        tokenRepository.save(member, refreshToken)

        return TokenPair(accessToken, refreshToken)
    }

    fun findRefreshToken(memberId: Long): RefreshToken? = tokenRepository.findValidRefToken(memberId)

    fun addBlackList(refreshToken: RefreshToken) {
        tokenRepository.addBlackList(refreshToken)
    }

    fun issueAccessToken(
        id: Long,
        role: Role,
    ): String = issue(id, role, jwtConfiguration.validation.access)

    fun issueRefreshToken(
        id: Long,
        role: Role,
    ): String = issue(id, role, jwtConfiguration.validation.refresh)

    private fun issue(
        id: Long,
        role: Role,
        expTime: Long,
    ): String {
        val now = Date()
        return Jwts
            .builder()
            .subject(id.toString())
            .claim("role", role)
            .issuedAt(Date())
            .expiration(Date(Date().time + expTime))
            .signWith(getSecretKey(), Jwts.SIG.HS256)
            .compact()
    }

    fun validate(token: String): Boolean {
        try {
            Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: SecurityException) {
            throw ErrorException(ErrorCode.INVALID_ACCESS_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw ErrorException(ErrorCode.INVALID_ACCESS_SIGNATURE)
        } catch (e: ExpiredJwtException) {
            throw ErrorException(ErrorCode.EXPIRED_ACCESS_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw ErrorException(ErrorCode.UNSUPPORTED_ACCESS_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw ErrorException(ErrorCode.INVALID_ACCESS_TOKEN)
        }
    }

    fun refreshValidate(token: String) {
        try {
            Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
        } catch (e: SecurityException) {
            throw ErrorException(ErrorCode.INVALID_REFRESH_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw ErrorException(ErrorCode.INVALID_REFRESH_SIGNATURE)
        } catch (e: ExpiredJwtException) {
            throw ErrorException(ErrorCode.EXPIRED_REFRESH_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw ErrorException(ErrorCode.UNSUPPORTED_REFRESH_TOKEN)
        }
    }

    fun parseJwt(token: String?): TokenBody {
        val parsed =
            Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)

        val sub = parsed.payload.subject
        val role = parsed.payload["role"].toString()

        return TokenBody(sub.toLong(), Role.valueOf(role))
    }

    private fun getSecretKey(): SecretKey = Keys.hmacShaKeyFor(jwtConfiguration.secrets.appKey.toByteArray())
}
