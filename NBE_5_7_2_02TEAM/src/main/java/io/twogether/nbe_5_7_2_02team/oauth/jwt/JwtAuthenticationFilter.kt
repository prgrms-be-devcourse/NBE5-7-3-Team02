package io.twogether.nbe_5_7_2_02team.oauth.jwt

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.oauth.service.OAuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val oAuthService: OAuthService,
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val token = resolveToken(request)

            if (token != null && jwtTokenProvider.validate(token)) {
                val tokenBody = jwtTokenProvider.parseJwt(token)
                val memberDetails =
                    oAuthService.getMemberDetailsById(tokenBody.memberId)

                val authentication =
                    UsernamePasswordAuthenticationToken(
                        memberDetails,
                        token,
                        memberDetails.authorities,
                    )

                SecurityContextHolder.getContext().authentication = authentication
            }

            filterChain.doFilter(request, response)
        } catch (e: ErrorException) {
            handleAuthTokenException(response, e)
        }
    }

    @Throws(IOException::class)
    private fun handleAuthTokenException(
        response: HttpServletResponse,
        e: ErrorException,
    ) {
        val message = e.errorCode.message

        response.apply {
            status = HttpServletResponse.SC_UNAUTHORIZED
            contentType = "application/json"
            characterEncoding = "UTF-8"
            writer.write(
                """
                {
                    "code": "${e.errorCode.code}",
                    "message": "$message"
                }
                """.trimIndent(),
            )
        }
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}
