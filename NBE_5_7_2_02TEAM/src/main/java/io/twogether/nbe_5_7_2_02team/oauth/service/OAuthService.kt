package io.twogether.nbe_5_7_2_02team.oauth.service

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.member.dao.MemberRepository
import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import io.twogether.nbe_5_7_2_02team.member.dto.request.SignUpRequest
import io.twogether.nbe_5_7_2_02team.member.dto.response.LoginResponse
import io.twogether.nbe_5_7_2_02team.member.dto.response.SignUpResponse
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.MemberDetails
import io.twogether.nbe_5_7_2_02team.oauth.dto.response.GitHubLoginResponse
import io.twogether.nbe_5_7_2_02team.oauth.dto.response.GitHubUserInfoResponse
import io.twogether.nbe_5_7_2_02team.oauth.jwt.JwtTokenProvider
import io.twogether.nbe_5_7_2_02team.oauth.jwt.MemberDetailsFactory
import net.minidev.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@Service
@Transactional
class OAuthService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository,
) : DefaultOAuth2UserService() {
    private val log = LoggerFactory.getLogger(OAuthService::class.java)
    private val restTemplate = RestTemplate()

    @Value("\${spring.security.oauth2.client.registration.github.client-id}")
    private lateinit var clientId: String

    @Value("\${spring.security.oauth2.client.registration.github.client-secret}")
    private lateinit var clientSecret: String

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        log.info("oAuth2User = {}", oAuth2User)
        val loginResponse = login(userRequest.accessToken.tokenValue)

        val providerId = userRequest.clientRegistration.registrationId.uppercase()

        val memberDetails = MemberDetailsFactory.memberDetails(providerId, oAuth2User)

        val member =
            memberRepository.findById(loginResponse.memberId)
                ?: throw ErrorException(ErrorCode.NOT_FOUND_MEMBER)

        memberDetails.id = member.id
        memberDetails.role = member.role

        return memberDetails
    }

    // GitHub에서 받은 code로 GitHub의 AccessToken 발급
    fun getAccessToken(code: String): GitHubLoginResponse {
        val accessTokenUrl = "https://github.com/login/oauth/access_token"

        val builder =
            UriComponentsBuilder
                .fromHttpUrl(accessTokenUrl)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)

        val headers =
            HttpHeaders().apply {
                accept =
                    listOf(MediaType.APPLICATION_JSON)
                contentType = MediaType.APPLICATION_JSON
            }

        return try {
            val entity = HttpEntity<String>(headers)
            val response =
                restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    entity,
                    JSONObject::class.java,
                )

            val body = response.body
            if (body != null && body.containsKey("access_token")) {
                log.info("GitHub access token obtained successfully")
                GitHubLoginResponse(body.getAsString("access_token"))
            } else {
                log.error("GitHub token response missing access_token: {}", body)
                throw ErrorException(ErrorCode.OAUTH_TOKEN_ERROR)
            }
        } catch (e: Exception) {
            log.error("Failed to get GitHub access token", e)
            throw ErrorException(ErrorCode.OAUTH_TOKEN_ERROR)
        }
    }

    // GitHub의 AccessToken을 이용하여 GitHub 사용자 정보 조회
    fun getUserInfo(accessToken: String): GitHubUserInfoResponse {
        val entity = HttpEntity<String>(createAuthHeaders(accessToken))

        val userInfo =
            getForObject("https://api.github.com/user", entity)
                ?: throw ErrorException(ErrorCode.OAUTH_USER_INFO_ERROR)

        // GitHub 사용자 ID
        val login = userInfo.getAsString("login")
        // GitHub 프로필 이미지 url
        val avatarUrl = userInfo.getAsString("avatar_url")
        val email = fetchPrimaryEmail(entity)
        val organizations = fetchOrganizations(entity)

        return GitHubUserInfoResponse(login, email, avatarUrl, organizations)
    }

    // GitHub 사용자의 기본 이메일
    private fun fetchPrimaryEmail(entity: HttpEntity<String>): String {
        val response =
            restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                entity,
                Array<Any>::class.java,
            )

        response.body?.let { emails ->
            for (emailObj in emails) {
                if (emailObj is Map<*, *>) {
                    val primary = emailObj["primary"] as? Boolean ?: false
                    val verified = emailObj["verified"] as? Boolean ?: false
                    val email = emailObj["email"] as? String

                    if (primary && verified && email != null) {
                        return email
                    }
                }
            }
        }

        throw ErrorException(ErrorCode.OAUTH_EMAIL_NOT_FOUND)
    }

    // GitHub 사용자의 organization 목록
    private fun fetchOrganizations(entity: HttpEntity<String>): List<String> {
        val response =
            restTemplate.exchange(
                "https://api.github.com/user/orgs",
                HttpMethod.GET,
                entity,
                Array<Any>::class.java,
            )

        val organizations = mutableListOf<String>()
        response.body?.let { orgs ->
            for (orgObj in orgs) {
                if (orgObj is Map<*, *>) {
                    val login = orgObj["login"] as? String
                    if (login != null) {
                        organizations.add(login)
                    }
                }
            }
        }

        return organizations
    }

    // GitHub에서 가져온 사용자 정보를 DB에 저장
    fun saveUserInfo(userInfo: GitHubUserInfoResponse): Member {
        validatePrgrmsOrganization(userInfo.organizations)

        val member =
            Member(Role.MEMBER, userInfo.email, userInfo.avatarUrl, userInfo.githubId)

        return memberRepository.save(member)
    }

    // GitHub의 Access Token으로 로그인 (첫 로그인 시 정보 저장)
    fun login(accessToken: String): LoginResponse {
        val userInfo = getUserInfo(accessToken)
        val member =
            memberRepository
                .findByEmail(userInfo.email)
                ?: saveUserInfo(userInfo)
        val tokenPair = jwtTokenProvider.generateTokenPair(member)
        return LoginResponse(
            tokenPair,
            member.role,
            member.id,
        )
    }

    // 추가 회원 가입 정보 등록
    fun signup(
        request: SignUpRequest,
        id: Long,
    ): SignUpResponse {
        val member =
            memberRepository.findById(id)
                ?: throw ErrorException(ErrorCode.NOT_FOUND_MEMBER)

        member.name = request.name
        member.job = request.job
        member.course = request.course

        return SignUpResponse.from(memberRepository.save(member))
    }

    private fun validatePrgrmsOrganization(organizations: List<String>) {
        val hasPrgrms =
            organizations
                .map { it.lowercase() }
                .any { it in ALLOWED_ORGS }

        if (!hasPrgrms) {
            throw OAuth2AuthenticationException("프로그래머스 교육 과정에 등록된 사용자만 가입할 수 있습니다.")
        }
    }

    fun getMemberDetailsById(id: Long): MemberDetails {
        val member =
            memberRepository.findById(id)
                ?: throw ErrorException(ErrorCode.NOT_FOUND_MEMBER)
        return MemberDetails.from(member)
    }

    // 인증 헤더 생성
    private fun createAuthHeaders(accessToken: String): HttpHeaders =
        HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "token $accessToken")
        }

    // 요청 URL에서 JSON 객체 받기
    private fun getForObject(
        url: String,
        entity: HttpEntity<String>,
    ): JSONObject? {
        val response =
            restTemplate.exchange(url, HttpMethod.GET, entity, JSONObject::class.java)
        return response.body
    }

    companion object {
        private val ALLOWED_ORGS =
            setOf(
                "prgrms-web-devcourse",
                "prgrms-be-devcourse",
                "prgrms-fe-devcourse",
                "prgrms-ad-devcourse",
                "prgrms-aibe-devcourse",
                "prgrms-app-devcourse",
                "prgrms-linux-devcourse",
                "prgrms-fullcycle-devcourse",
            )
    }
}
