package io.twogether.nbe_5_7_2_02team.chat.dao.adapter

import io.twogether.nbe_5_7_2_02team.chat.dao.ChatMemberRepository
import io.twogether.nbe_5_7_2_02team.chat.dao.ChatMessageRepository
import io.twogether.nbe_5_7_2_02team.chat.dao.ChatRepository
import io.twogether.nbe_5_7_2_02team.chat.dao.ChatRoomRepository
import io.twogether.nbe_5_7_2_02team.chat.domain.ChatRoom
import io.twogether.nbe_5_7_2_02team.post.domain.Post
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class ChatRepositoryAdapter (
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMemberRepository: ChatMemberRepository,
    private val chatMessageRepository: ChatMessageRepository,
) : ChatRepository {

    override fun deleteByPost(post: Post) {
        chatRoomRepository.findByPost(post) ?:
                { chatRoom: ChatRoom ->
                    chatMessageRepository.deleteByChatRoom(chatRoom)
                    chatMemberRepository.deleteByChatRoom(chatRoom)
                    chatRoomRepository.delete(chatRoom)
                }
    }
}
