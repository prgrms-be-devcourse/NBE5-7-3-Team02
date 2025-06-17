package io.twogether.nbe_5_7_2_02team.global.config

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.oauth.jwt.JwtTokenProvider
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val jwtTokenProvider: JwtTokenProvider
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/sub")
        config.setApplicationDestinationPrefixes("/pub")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws/chatroom").setAllowedOriginPatterns("*").withSockJS()
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
                    ?: return message

                if (StompCommand.CONNECT == accessor.command) {
                    val token = accessor.getFirstNativeHeader("Authorization")?.substringAfter("Bearer ")

                    token?.let {
                        try {
                            if (jwtTokenProvider.validate(it)) {
                                val tokenBody = jwtTokenProvider.parseJwt(it)
                                val authorities = listOf(SimpleGrantedAuthority("ROLE_${tokenBody.role.name}"))
                                val authentication = UsernamePasswordAuthenticationToken(tokenBody, null, authorities)
                                accessor.user = authentication
                            }
                        } catch (e: ErrorException) {
                            throw ErrorException(ErrorCode.INVALID_ACCESS_TOKEN)
                        }
                    } ?: throw ErrorException(ErrorCode.INVALID_ACCESS_TOKEN)
                }
                return message
            }
        })
    }
}