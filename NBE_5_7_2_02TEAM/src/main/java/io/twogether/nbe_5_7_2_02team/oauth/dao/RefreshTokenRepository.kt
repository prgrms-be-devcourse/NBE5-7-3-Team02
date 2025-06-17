package io.twogether.nbe_5_7_2_02team.oauth.dao

import io.twogether.nbe_5_7_2_02team.oauth.domain.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByMemberId(memberId: Long): RefreshToken?
}
