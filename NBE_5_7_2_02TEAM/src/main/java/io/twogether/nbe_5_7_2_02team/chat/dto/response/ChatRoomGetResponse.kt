package io.twogether.nbe_5_7_2_02team.chat.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import java.time.LocalDateTime

@Schema(description = "채팅방 조회 응답 DTO")
data class ChatRoomGetResponse(
    @field:Schema(description = "채팅방 ID", example = "1")
    val id: Long,
    @field:Schema(description = "게시글 ID", example = "10")
    val postId: Long,
    @field:Schema(description = "게시글 제목", example = "서버 개발 스터디 모집")
    val title: String,
    @field:Schema(description = "채팅방 참가자 수", example = "5")
    val memberCount: Long,
    @field:Schema(description = "채팅방 최근 수정 시간", example = "2025-06-18T15:30:00")
    val updatedAt: LocalDateTime,
)

fun ChatRoom.toGetResponse(): ChatRoomGetResponse =
    ChatRoomGetResponse(
        id = this.id,
        postId = this.post.id!!,
        title = this.post.title,
        memberCount = this.memberCount,
        updatedAt = this.updatedAt,
    )
