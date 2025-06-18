package io.twogether.nbe_5_7_2_02team.global.config

import io.twogether.nbe_5_7_2_02team.oauth.jwt.JwtAuthenticationFilter
import io.twogether.nbe_5_7_2_02team.oauth.jwt.OAuth2FailureHandler
import io.twogether.nbe_5_7_2_02team.oauth.jwt.OAuth2SuccessHandler
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val oAuth2FailureHandler: OAuth2FailureHandler,
    @Value("\${management.endpoints.web.base-path}")
    private val actuatorBasePath: String,
) {
    companion object {
        private val log = KotlinLogging.logger {}

        private val ALLOWED_ORIGINS =
            listOf(
                "http://localhost:5173",
                "http://localhost:8080",
            )

        private val ALLOWED_METHODS =
            listOf(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS",
            )

        private val PUBLIC_ENDPOINTS =
            arrayOf(
                "/ws/chatroom/**",
                "/api/chatroom/**",
                "/api/tags/**",
                "/api/oauth2/**",
                "/api/tags",
                "/api/token/**",
            )

        private const val CACHE_PERIOD = 3600
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.run {
            httpBasic { it.disable() }
            csrf { it.disable() }
            cors { it.configurationSource(corsConfigurationSource()) }
            formLogin { it.disable() }
            sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            oauth2Login { oauth ->
                oauth.successHandler(oAuth2SuccessHandler)
                oauth.failureHandler(oAuth2FailureHandler)
            }
            exceptionHandling {
                it.authenticationEntryPoint(customAuthenticationEntryPoint())
            }
            authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        RequestMatcher { request ->
                            CorsUtils.isPreFlightRequest(request)
                        },
                    ).permitAll()
                    .requestMatchers("$actuatorBasePath/**")
                    .hasRole("PROMETHEUS")
                    .requestMatchers("/api/chatroom/entered")
                    .authenticated()
                    .requestMatchers(*PUBLIC_ENDPOINTS)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/posts")
                    .permitAll()
                    .requestMatchers("/api/**")
                    .hasAnyAuthority("MEMBER")
                    .anyRequest()
                    .permitAll()
            }
            addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            build()
        }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config =
            CorsConfiguration().apply {
                allowCredentials = true
                allowedOrigins = ALLOWED_ORIGINS
                allowedMethods = ALLOWED_METHODS
                allowedHeaders = listOf("*")
            }

        log.info { "CORS 설정 동작: ${config.allowedOrigins}" }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

    @Bean
    fun customAuthenticationEntryPoint(): AuthenticationEntryPoint =
        AuthenticationEntryPoint { request, response, _ ->
            // API 요청은 401, 그 외는 기존처럼 GitHub 로그인 페이지 리다이렉트
            if (request.requestURI.startsWith("/api/")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            } else {
                response.sendRedirect("/oauth2/authorization/github")
            }
        }
}
