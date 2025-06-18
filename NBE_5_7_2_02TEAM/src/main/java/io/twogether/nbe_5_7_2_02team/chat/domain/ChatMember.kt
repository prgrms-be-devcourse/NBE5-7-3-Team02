package io.twogether.nbe_5_7_2_02team.chat.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import jakarta.persistence.*

@Entity
data class ChatMember(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:JoinColumn(name = "chatroom_id")
    @field:ManyToOne
    val chatRoom: ChatRoom,
    @field:JoinColumn(name = "member_id")
    @field:ManyToOne
    val member: Member,
    @field:Enumerated(EnumType.STRING)
    @field:Column(name = "status")
    var chatMemberStatus: ChatMemberStatus = ChatMemberStatus.ONLINE,
) : BaseEntity()
