package io.twogether.nbe_5_7_2_02team.chat.domain

import io.twogether.nbe_5_7_2_02team.global.common.BaseEntity
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import jakarta.persistence.*

@Entity
data class ChatRoom(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @field:JoinColumn(name = "post_id")
    @field:OneToOne
    val post: Post,
) : BaseEntity() {
    @Column(name = "member_count")
    var memberCount: Long = 0L

    @Column(name = "last_chat_id")
    var lastChatId: Long = 0L
}
