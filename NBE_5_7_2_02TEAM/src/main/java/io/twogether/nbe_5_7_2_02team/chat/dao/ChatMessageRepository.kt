package io.twogether.nbe_5_7_2_02team.chat.dao

import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMember
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMessage
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findByChatRoomOrderByCreatedAtAsc(chatroom: ChatRoom): MutableList<ChatMessage>

    fun findByIdAndChatRoomAndChatMember(
        id: Long,
        chatRoom: ChatRoom,
        chatMember: ChatMember,
    ): ChatMessage?

    fun deleteByChatRoom(chatRoom: ChatRoom)
}
