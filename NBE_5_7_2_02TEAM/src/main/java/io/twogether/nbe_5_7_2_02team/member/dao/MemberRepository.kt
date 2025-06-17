package io.twogether.nbe_5_7_2_02team.member.dao

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member?, Long?> {
    fun findByEmail(email: String?): Member?

    fun findById(id: Long): Member?
}
