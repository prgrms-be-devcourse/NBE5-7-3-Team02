package io.twogether.nbe_5_7_2_02team.oauth.jwt

import io.twogether.nbe_5_7_2_02team.global.exception.ErrorException
import io.twogether.nbe_5_7_2_02team.global.response.error.ErrorCode
import io.twogether.nbe_5_7_2_02team.oauth.dto.common.MemberDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*

object MemberDetailsFactory {
    fun memberDetails(provider: String, oAuth2User: OAuth2User): MemberDetails {
        val attributes = oAuth2User.attributes

        when (provider.uppercase().trim()) {
            "GITHUB" -> {
                return MemberDetails.builder()
                    .name(attributes["login"].toString())
                    .attributes(attributes)
                    .build()
//                return MemberDetails(
//                    name = attributes["login"].toString(),
//                    attributes = attributes
//                )
            }

            else -> throw ErrorException(ErrorCode.UNSUPPORTED_PROVIDER)
        }
    }
}
