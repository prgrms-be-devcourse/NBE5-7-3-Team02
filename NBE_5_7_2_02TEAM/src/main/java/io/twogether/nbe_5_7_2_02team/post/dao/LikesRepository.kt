package io.twogether.nbe_5_7_2_02team.post.dao

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.post.domain.Likes
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LikesRepository : JpaRepository<Likes, Long> {
    fun findByPostAndMember(
        post: Post,
        member: Member,
    ): Optional<Likes>

    fun deleteByPost(post: Post)
}
