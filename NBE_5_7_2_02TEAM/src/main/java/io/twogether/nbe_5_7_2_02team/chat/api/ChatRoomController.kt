package io.twogether.nbe_5_7_2_02team.chat.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatRoomGetResponse
import io.twogether.nbe_5_7_2_02team.chat.service.ChatRoomService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chatroom")
@Tag(name = "ChatRoom", description = "채팅방 관련 API")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {
    @GetMapping
    @Operation(
        summary = "전체 채팅방 목록 조회",
        description = "현재 존재하는 모든 채팅방 목록을 조회합니다."
    )
    fun getChatRoomList(): ResponseEntity<List<ChatRoomGetResponse>> {
        val chatRoomGetResponse = chatRoomService.getChatRoomList()

        return ResponseEntity.ok(chatRoomGetResponse)
    }

    @GetMapping("/{postId}")
    @Operation(
        summary = "게시글 ID로 채팅방 조회",
        description = "특정 게시글에 연관된 채팅방 정보를 조회합니다."
    )
    fun getChatRoomByPost(
        @PathVariable("postId") postId: Long,
    ): ResponseEntity<ChatRoomGetResponse> {
        val chatRoomGetResponse = chatRoomService.getChatRoomByPost(postId)

        return ResponseEntity.ok(chatRoomGetResponse)
    }

    @PostMapping("/{postId}")
    @Operation(
        summary = "채팅방 생성",
        description = "특정 게시글(postId)을 기반으로 새로운 채팅방을 생성합니다."
    )
    fun createChatRoom(
        @PathVariable("postId") postId: Long,
    ): ResponseEntity<Long> {
        val chatRoomId = chatRoomService.createChatroom(postId)

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomId)
    }
}
