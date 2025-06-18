package io.twogether.nbe_5_7_2_02team.chat.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.twogether.nbe_5_7_2_02team.chat.dto.request.ChatMessagePostRequest
import io.twogether.nbe_5_7_2_02team.chat.dto.response.ChatMessageGetResponse
import io.twogether.nbe_5_7_2_02team.chat.service.ChatMessageService
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.TokenBody
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/chatroom")
@Tag(name = "ChatMessage", description = "채팅 메시지 API")
class ChatMessageController(
    private val chatMessageService: ChatMessageService,
) {
    @GetMapping("/{chatroomId}/message")
    @Operation(
        summary = "채팅 메시지 목록 조회",
        description = "지정한 채팅방에 저장된 모든 채팅 메시지를 시간순으로 조회합니다.",
    )
    fun getChatMessageList(
        @PathVariable chatroomId: Long,
    ): ResponseEntity<List<ChatMessageGetResponse>> {
        val chatMessage = chatMessageService.getChatMessage(chatroomId)

        return ResponseEntity.ok(chatMessage)
    }

    @MessageMapping("/{chatroomId}/message")
    @SendTo("/sub/{chatroomId}/message")
    @Operation(
        summary = "채팅 메시지 전송 (WebSocket)",
        description = """
            지정한 채팅방으로 메시지를 전송합니다.
            WebSocket 통신을 사용하며, 해당 채팅방 구독자에게 메시지를 브로드캐스팅합니다.
        """,
    )
    fun createChatMessage(
        @DestinationVariable chatroomId: Long,
        @Payload chatMessagePostRequest: ChatMessagePostRequest,
        principal: Principal,
    ): ChatMessageGetResponse {
        var tokenBody: TokenBody?
        var memberId: Long? = null

        if (principal is Authentication) {
            val authentication = principal
            val authPrincipal = authentication.principal

            if (authPrincipal is TokenBody) {
                tokenBody = authPrincipal
                memberId = tokenBody.memberId
            }
        }

        val chatMessageGetResponse =
            chatMessageService.createChatMessage(chatroomId, chatMessagePostRequest, memberId)

        return chatMessageGetResponse
    }

    @DeleteMapping("/{chatroomId}/message")
    @Operation(
        summary = "채팅 메시지 삭제",
        description = "지정한 채팅방의 특정 메시지를 삭제합니다.",
    )
    fun deleteChatMessage(
        @PathVariable chatroomId: Long,
        @RequestParam chatMessageId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        chatMessageService.deleteChatMessage(chatMessageId, chatroomId, userDetails)

        return ResponseEntity.ok(chatroomId)
    }
}
