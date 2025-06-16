package io.twogether.nbe_5_7_2_02team.post.dao

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.post.domain.PostApplication
import io.twogether.nbe_5_7_2_02team.post.domain.RecruitmentField
import org.springframework.data.jpa.repository.JpaRepository

interface PostApplicationRepository : JpaRepository<PostApplication, Long> {
    fun existsByMemberAndField(
        member: Member,
        field: RecruitmentField,
    ): Boolean
}
