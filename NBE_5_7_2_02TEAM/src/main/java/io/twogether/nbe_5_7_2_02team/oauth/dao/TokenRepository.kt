package io.twogether.nbe_5_7_2_02team.oauth.dao

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshToken
import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshTokenBlackList
import java.util.*

interface TokenRepository {
    fun save(
        member: Member,
        token: String,
    ): RefreshToken

    fun addBlackList(refreshToken: RefreshToken): RefreshTokenBlackList?

    fun findValidRefToken(memberId: Long): RefreshToken?
}
