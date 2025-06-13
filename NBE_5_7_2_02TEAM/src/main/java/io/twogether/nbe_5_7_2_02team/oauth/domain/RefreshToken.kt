package io.twogether.nbe_5_7_2_02team.oauth.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import jakarta.persistence.*

@Entity
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val refreshToken: String,

    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member
) : BaseEntity()
