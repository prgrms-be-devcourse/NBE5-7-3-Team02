package io.twogether.nbe_5_7_2_02team.oauth.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
class RefreshToken(
    var refreshToken: String,

    @field:JoinColumn(name = "member_id")
    @field:ManyToOne
    var member: Member
) :
    BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
