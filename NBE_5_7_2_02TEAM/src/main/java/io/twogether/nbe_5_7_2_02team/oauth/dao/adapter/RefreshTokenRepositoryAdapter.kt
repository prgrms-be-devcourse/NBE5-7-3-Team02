package io.twogether.nbe_5_7_2_02team.oauth.dao.adapter

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenBlackListRepository
import io.twogether.nbe_5_7_2_02team.oauth.dao.RefreshTokenRepository
import io.twogether.nbe_5_7_2_02team.oauth.dao.TokenRepository
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshToken
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshTokenBlackList
import jakarta.persistence.EntityManager
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RefreshTokenRepositoryAdapter(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val refreshTokenBlackListRepository: RefreshTokenBlackListRepository,
    private val entityManager: EntityManager
) : TokenRepository {
    override fun save(member: Member, token: String): RefreshToken {
        return refreshTokenRepository.save(
            RefreshToken(token, member)
        )
    }

    override fun addBlackList(refreshToken: RefreshToken): RefreshTokenBlackList {
        return refreshTokenBlackListRepository.save(
            RefreshTokenBlackList(refreshToken)
        )
    }

    override fun findValidRefToken(memberId: Long): RefreshToken? {
        val query = entityManager.createQuery(
            """
            select rf from RefreshToken rf 
            left join RefreshTokenBlackList rtb 
            on rtb.refreshToken = rf 
            where rf.member.id = :memberId 
            and rtb.id is null
            """.trimIndent(), RefreshToken::class.java
        )

        query.setParameter("memberId", memberId)

        return query.resultStream.findFirst().orElse(null)
    }
}
