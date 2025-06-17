package io.twogether.nbe_5_7_2_02team.post.domain

import io.twogether.nbe_5_7_2_02team.tag.domain.Tag
import jakarta.persistence.*
import lombok.Builder

@Entity
class PostTag (
    @field:JoinColumn(name = "post_id")
    @field:ManyToOne(
        fetch = FetchType.LAZY
    ) val post: Post,

    @field:JoinColumn(name = "tag_id")
    @field:ManyToOne(
        fetch = FetchType.LAZY
    ) val tag: Tag
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
