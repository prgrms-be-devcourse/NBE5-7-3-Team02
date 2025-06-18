package io.twogether.nbe_5_7_2_02team.chat.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
data class ChatMessage(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:JoinColumn(name = "chatroom_id")
    @field:ManyToOne
    val chatRoom: ChatRoom,
    @field:JoinColumn(name = "chatroom_member_id")
    @field:ManyToOne
    val chatMember: ChatMember,
    @field:Column(name = "content")
    val content: String,
) : BaseEntity()
