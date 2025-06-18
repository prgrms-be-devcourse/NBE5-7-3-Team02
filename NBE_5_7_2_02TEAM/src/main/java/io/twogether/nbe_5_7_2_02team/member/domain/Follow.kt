package io.twogether.nbe_5_7_2_02team.member.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
class Follow(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    var follower: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    var following: Member

) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

}
