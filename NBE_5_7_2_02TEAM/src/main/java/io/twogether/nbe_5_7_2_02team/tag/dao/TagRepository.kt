package io.twogether.nbe_5_7_2_02team.tag.dao

import io.twogether.nbe_5_7_2_02team.tag.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface TagRepository : JpaRepository<Tag, Long> {

    fun findByName(name: String): Tag?

    @Modifying
    @Query("DELETE FROM Tag t WHERE t.id NOT IN :referredTagIds")
    fun deleteUnusedTags(referredTagIds: List<Long>)
}
