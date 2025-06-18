package io.twogether.nbe_5_7_2_02team.oauth.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class RefreshTokenBlackList(@field:JoinColumn(name = "refresh_token_id") @field:ManyToOne private var refreshToken: RefreshToken) :
    BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}
