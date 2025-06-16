package io.twogether.nbe_5_7_2_02team.member.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:[Column(nullable = false) Email]
    var email: String,

    @Column(nullable = false)
    var githubId: String,

    @Column(nullable = false)
    var name: String,
    var profileImage: String?,

    @Column(nullable = false)
    var job: String,
    @Column(nullable = false)
    var course: String,

    @Enumerated(EnumType.STRING)
    var role: Role

) : BaseEntity() {

    fun updateProfile(nickname: String?, profileImageUrl: String?) {
        if (!nickname.isNullOrBlank()) {
            name = nickname
        }
        if (!profileImageUrl.isNullOrBlank()) {
            profileImage = profileImageUrl
        }
    }

    /*PA no-arg 플러그인을 사용해도 기본값이 0 으로 세팅 → DB PK 충돌 위험 : id값만 비교해서 안전하게 구현*/
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}
