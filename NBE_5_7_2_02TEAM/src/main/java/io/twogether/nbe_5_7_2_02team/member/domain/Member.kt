package io.twogether.nbe_5_7_2_02team.member.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
class Member(
    @field:Column(nullable = false)
    var email: String,
    var name: String = "",
    var profileImage: String?,
    var job: String = "",
    var course: String = "",
    @field:Column(nullable = false)
    var githubId: String,
    @field:Enumerated(EnumType.STRING)
    var role: Role = Role.MEMBER,
) : BaseEntity() {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateProfile(
        nickname: String,
        profileImageUrl: String?,
    ) {
        if (nickname.isNotBlank()) {
            this.name = nickname
        }
        this.profileImage = profileImageUrl
    }
}
