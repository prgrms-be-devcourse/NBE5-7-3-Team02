package io.twogether.nbe_5_7_2_02team.oauth.jwt

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.MemberDetails
import io.twogether.nbe_5_7_2_02team.oauth.service.OAuthService
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

@Component
class OAuth2SuccessHandler(
    private val memberRepository: MemberRepository,
    private val oAuthService: OAuthService,
    private val jwtTokenProvider: JwtTokenProvider
) : SimpleUrlAuthenticationSuccessHandler() {

    @Value("\${custom.jwt.redirection.base}")
    private val baseUrl: String? = null

    private val log = LoggerFactory.getLogger(OAuth2SuccessHandler::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication
    ) {
        val principal = authentication.principal as MemberDetails
        val findMember = memberRepository.findById(principal.id!!)
            ?: throw ErrorException(ErrorCode.NOT_FOUND_MEMBER)

        val params = HashMap<String, String>()

        val refreshTokenOptional =
            jwtTokenProvider.findRefreshToken(principal.id!!)

        if (refreshTokenOptional == null) {
            val tokenPair = jwtTokenProvider.generateTokenPair(findMember)
            params["access"] = tokenPair.accessToken
            params["refresh"] = tokenPair.refreshToken
        } else {
            val accessToken =
                jwtTokenProvider.issueAccessToken(principal.id!!, principal.role!!)
            params["access"] = accessToken
            params["refresh"] = refreshTokenOptional.refreshToken
        }

        val urlStr = genUrlStr(params)
        redirectStrategy.sendRedirect(request, response, urlStr)
    }

    private fun genUrlStr(params: HashMap<String, String>): String {
        return UriComponentsBuilder.fromUriString(baseUrl!!)
            .queryParam("accessToken", params["access"])
            .queryParam("refreshToken", params["refresh"])
            .build()
            .toUri()
            .toString()
    }
}
