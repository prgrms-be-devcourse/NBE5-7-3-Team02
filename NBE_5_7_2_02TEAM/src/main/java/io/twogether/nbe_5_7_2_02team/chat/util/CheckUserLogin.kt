package io.twogether.nbe_5_7_2_02team.chat.util

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.function.Supplier

@Component
@RequiredArgsConstructor
class CheckUserLogin (
    private val memberRepository: MemberRepository
) {

    fun checkUserLogin(userDetails: UserDetails?): Member {
        if (userDetails == null) {
            throw ErrorException(ErrorCode.CHAT_MEMBER_NOT_LOGIN)
        }

        return memberRepository
            .findById(userDetails.username.toLong())
            .orElseThrow(Supplier { ErrorException(ErrorCode.NOT_FOUND_MEMBER) })
    }
}
