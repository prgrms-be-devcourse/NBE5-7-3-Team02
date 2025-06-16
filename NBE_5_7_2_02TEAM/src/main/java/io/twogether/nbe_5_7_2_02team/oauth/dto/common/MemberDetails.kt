package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import lombok.*
import lombok.experimental.Accessors
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.List

class MemberDetails(
    var id: Long? = null,
    var name: String? = null,
    var email: String? = null,
    var role: Role? = null,
    var attributes: Map<String, Any>? = null,
    var githubId: String? = null,
    var avatarUrl: String? = null
) : OAuth2User, UserDetails {


    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority(role?.name ?: "ROLE_MEMBER"))
    }

    override fun getPassword(): String? = null

    override fun getUsername(): String = id.toString()

    override fun getName(): String? = name

    override fun getAttributes(): Map<String, Any>? = attributes

    companion object {
        fun from(member: Member): MemberDetails {
            return MemberDetails(
                id = member.id,
                name = member.name,
                email = member.email,
                role = member.role
            )
        }
    }
}
