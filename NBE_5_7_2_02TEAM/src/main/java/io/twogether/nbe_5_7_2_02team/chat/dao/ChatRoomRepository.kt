package io.twogether.nbe_5_7_2_02team.chat.dao

import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository : JpaRepository<ChatRoom, Long> {
    fun findChatRoomById(id: Long): ChatRoom?

    fun findByPost(post: Post): ChatRoom?

    fun deleteByPost(post: Post)
}
