package io.twogether.nbe_5_7_2_02team.post.domain

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import jakarta.persistence.*

@Entity
class PostApplication(
    @field:JoinColumn(
        name = "member_id",
        nullable = false,
    )
    @field:ManyToOne(fetch = FetchType.LAZY)
    val member: Member,
    @field:JoinColumn(
        name = "post_id",
        nullable = false,
    )
    @field:ManyToOne(fetch = FetchType.LAZY)
    val post: Post,
    @field:JoinColumn(
        name = "field_id",
        nullable = false,
    )
    @field:ManyToOne(fetch = FetchType.LAZY)
    val field: RecruitmentField,
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
