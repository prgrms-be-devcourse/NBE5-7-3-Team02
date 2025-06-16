package io.twogether.nbe_5_7_2_02team.tag.domain

import io.twogether.nbe_5_7_2_02team.post.domain.PostTag
import jakarta.persistence.*

@Entity
class Tag(
    @Column(nullable = false)
    val name: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    val postTags: MutableList<PostTag> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tag) return false
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}
