package io.twogether.nbe_5_7_2_02team.oauth.jwt

import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "custom.jwt")
class JwtConfiguration(
    val validation: Validation,
    val secrets: Secrets
) {

    data class Validation(
        val access: Long,
        val refresh: Long
    )

    data class Secrets(
        val originKey: String,
        val appKey: String
    )
}
