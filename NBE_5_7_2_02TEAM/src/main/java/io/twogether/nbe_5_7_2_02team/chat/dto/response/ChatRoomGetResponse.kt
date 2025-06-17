package io.twogether.nbe_5_7_2_02team.chat.dto.response

import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import java.time.LocalDateTime

data class ChatRoomGetResponse(
    val id: Long,
    val postId: Long,
    val title: String,
    val memberCount: Long,
    val updatedAt: LocalDateTime,
)

fun ChatRoom.toGetResponse(): ChatRoomGetResponse =
    ChatRoomGetResponse(
        id = this.id,
        postId = this.post.id,
        title = this.post.title,
        memberCount = this.memberCount,
        updatedAt = this.updatedAt,
    )
