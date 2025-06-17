package io.twogether.nbe_5_7_2_02team.post.domain

import jakarta.persistence.*
import jakarta.persistence.Column
import lombok.AccessLevel
import lombok.Builder
import lombok.NoArgsConstructor

@Entity
class RecruitmentField (
    @field:JoinColumn(name = "post_id")
    @field:ManyToOne(
        fetch = FetchType.LAZY
    ) var post: Post?,

    @field:Column(nullable = false)
    var fieldName: String,

    @field:Column(nullable = false)
    var totalCount: Int,

    @field:Column(nullable = false)
    var currentCount: Int,

    @field:Column(nullable = false)
    var closed: Boolean
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun increaseCount() {
        currentCount++
        if (currentCount >= totalCount) {
            closed = true
        }
    }
}
