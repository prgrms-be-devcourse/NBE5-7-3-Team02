package io.twogether.nbe_5_7_2_02team.chat.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import jakarta.persistence.*

@Entity
data class ChatMember(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @JoinColumn(name = "chatroom_id")
    @ManyToOne
    val chatRoom: ChatRoom,
    @JoinColumn(name = "member_id")
    @ManyToOne
    val member: Member,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var chatMemberStatus: ChatMemberStatus = ChatMemberStatus.ONLINE,
) : BaseEntity()
