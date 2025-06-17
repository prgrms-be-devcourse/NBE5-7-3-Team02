package io.twogether.nbe_5_7_2_02team.oauth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class OAuth2FailureHandler : AuthenticationFailureHandler {
    @Value("\${custom.jwt.redirection.base}")
    private lateinit var baseUrl: String

    private val objectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(OAuth2FailureHandler::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        log.error("OAuth 인증 실패: {}", exception.message)

        response.sendRedirect("$baseUrl?error=org")
    }
}
