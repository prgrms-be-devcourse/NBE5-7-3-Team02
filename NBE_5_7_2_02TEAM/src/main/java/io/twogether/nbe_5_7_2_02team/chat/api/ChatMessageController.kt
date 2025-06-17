package io.twogether.nbe_5_7_2_02team.chat.api

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
class ChatMessageController(
    private val chatMessageService: ChatMessageService,
) {
    @GetMapping("/{chatroomId}/message")
    fun getChatMessageList(
        @PathVariable chatroomId: Long,
    ): ResponseEntity<List<ChatMessageGetResponse>> {
        val chatMessage = chatMessageService.getChatMessage(chatroomId)

        return ResponseEntity.ok(chatMessage)
    }

    @MessageMapping("/{chatroomId}/message")
    @SendTo("/sub/{chatroomId}/message")
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
    fun deleteChatMessage(
        @PathVariable chatroomId: Long,
        @RequestParam chatMessageId: Long,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): ResponseEntity<Long> {
        chatMessageService.deleteChatMessage(chatMessageId, chatroomId, userDetails)

        return ResponseEntity.ok(chatroomId)
    }
}
