package io.twogether.nbe_5_7_2_02team.chat.dto.response

import io.twogether.nbe_5_7_2_02team.chat.domain.ChatMessage
import java.time.LocalDateTime

data class ChatMessageGetResponse(
    val id: Long,
    val memberId: Long,
    val memberName: String,
    val content: String,
    val createdAt: LocalDateTime,
)

fun ChatMessage.toGetResponse(): ChatMessageGetResponse =
    ChatMessageGetResponse(
        id = this.id!!,
        memberId = this.chatMember.member.id,
        memberName = this.chatMember.member.name,
        content = this.content,
        createdAt = this.createdAt,
    )
