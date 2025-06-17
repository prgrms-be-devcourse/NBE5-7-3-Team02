package io.twogether.nbe_5_7_2_02team.post.dao

import io.twogether.nbe_5_7_2_02team.post.domain.Post
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface PostRepository :
    JpaRepository<Post, Long>,
    PostRepositoryFilter {
    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId")
    fun findAllByMemberId(
        @Param("memberId") memberId: Long,
    ): List<Post>

    fun findByRecruitmentStatusAndRecruitmentDeadlineBefore(
        status: RecruitmentStatus,
        deadline: LocalDate,
    ): List<Post>
}
