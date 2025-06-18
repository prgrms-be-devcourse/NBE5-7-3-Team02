package io.twogether.nbe_5_7_2_02team.post.dao

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.post.domain.Likes
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface LikesRepository : JpaRepository<Likes, Long> {
    fun existsByPostAndMember(
        post: Post,
        member: Member,
    ): Boolean

    fun findByPostAndMember(
        post: Post,
        member: Member,
    ): Likes?

    fun deleteByPost(post: Post)
}
