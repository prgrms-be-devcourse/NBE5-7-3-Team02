package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class MemberDetails(
    var id: Long? = null,
    private val _name: String? = null,
    var email: String? = null,
    var role: Role? = null,
    private var _attributes: Map<String, Any>? = null,
    var githubId: String? = null,
    var avatarUrl: String? = null
) : OAuth2User, UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return role?.let { listOf(SimpleGrantedAuthority(it.name)) } ?: emptyList()
    }

    override fun getPassword(): String? = null

    override fun getUsername(): String = id?.toString() ?: ""

    // OAuth2User interface methods
    override fun getName(): String? = _name

    override fun getAttributes(): Map<String, Any> = _attributes ?: emptyMap()

    companion object {
        fun from(member: Member): MemberDetails {
            return MemberDetails(
                id = member.id,
                _name = member.name,
                email = member.email,
                role = member.role
            )
        }
    }
}