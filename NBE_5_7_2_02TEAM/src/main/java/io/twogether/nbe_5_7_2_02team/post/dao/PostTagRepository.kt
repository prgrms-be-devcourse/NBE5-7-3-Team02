package io.twogether.nbe_5_7_2_02team.post.dao

import io.twogether.nbe_5_7_2_02team.post.domain.Post
import io.twogether.nbe_5_7_2_02team.post.domain.PostTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostTagRepository : JpaRepository<PostTag, Long> {
    fun deleteAllByPost(post: Post)

    @Query("SELECT DISTINCT pt.tag.id FROM PostTag pt")
    fun findAllTagIds(): List<Long>
}