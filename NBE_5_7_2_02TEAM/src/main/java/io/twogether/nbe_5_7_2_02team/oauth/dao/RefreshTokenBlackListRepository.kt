package io.twogether.nbe_5_7_2_02team.oauth.dao

import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshToken
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshTokenBlackList
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenBlackListRepository : JpaRepository<RefreshTokenBlackList, Long> {
    fun deleteByRefreshToken(refreshToken: RefreshToken)

    fun findByRefreshTokenId(refreshTokenId: Long?): RefreshTokenBlackList?
}
