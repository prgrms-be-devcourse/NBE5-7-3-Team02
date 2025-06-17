package io.twogether.nbe_5_7_2_02team.chat.dao

import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMember
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMemberStatus
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMemberRepository : JpaRepository<ChatMember, Long> {
    fun findByChatRoomAndMember(
        chatRoom: ChatRoom,
        member: Member,
    ): ChatMember?

    fun findByChatRoom(chatRoom: ChatRoom): List<ChatMember>

    fun findByMemberAndChatMemberStatusIn(
        member: Member,
        chatMemberStatuses: Collection<ChatMemberStatus>,
    ): List<ChatMember>

    fun deleteByChatRoom(chatRoom: ChatRoom)

    fun countByChatRoom(chatRoom: ChatRoom): Long
}
