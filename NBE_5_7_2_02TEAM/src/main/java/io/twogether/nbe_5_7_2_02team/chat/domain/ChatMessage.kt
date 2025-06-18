package io.twogether.nbe_5_7_2_02team.chat.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import jakarta.persistence.*

@Entity
class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JoinColumn(name = "chatroom_id")
    @ManyToOne
    val chatRoom: ChatRoom,

    @JoinColumn(name = "chatroom_member_id")
    @ManyToOne
    val chatMember: ChatMember,

    @Column(name = "content")
    val content: String
) : BaseEntity()