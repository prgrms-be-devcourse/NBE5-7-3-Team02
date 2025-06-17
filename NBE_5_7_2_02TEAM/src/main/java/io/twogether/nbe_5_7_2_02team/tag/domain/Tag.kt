package io.twogether.nbe_5_7_2_02team.tag.domain

import io.twogether.nbe_5_7_2_02team.post.domain.PostTag
import jakarta.persistence.*

@Entity
class Tag (
    @field:Column(nullable = false)
    var name: String,
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
