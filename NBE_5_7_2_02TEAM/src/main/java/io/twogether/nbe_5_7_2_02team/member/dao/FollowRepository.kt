package io.twogether.nbe_5_7_2_02team.member.dao

import io.twogether.nbe_5_7_2_02team.member.domain.Follow
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FollowRepository : JpaRepository<Follow, Long> {
    @Query("SELECT f.follower FROM Follow f where f.following = :member")
    fun findFollowerMembers(
        @Param("member") member: Member,
        pageable: Pageable,
    ): Page<Member>

    @Query("select f.following from Follow f where f.follower = :member")
    fun findFollowingMembers(
        @Param("member") member: Member,
        pageable: Pageable,
    ): Page<Member>

    fun existsByFollowerAndFollowing(
        follower: Member,
        following: Member,
    ): Boolean

    fun countByFollower(follower: Member): Long

    fun countByFollowing(following: Member): Long

    fun deleteByFollowerAndFollowing(
        follower: Member,
        following: Member,
    ): Long
}
