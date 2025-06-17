package io.twogether.nbe_5_7_2_02team.oauth.dto.common

import io.twogether.nbe_5_7_2_02team.member.domain.Member
import io.twogether.nbe_5_7_2_02team.member.domain.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class MemberDetails(
    var id: Long? = null,
    private val name: String? = null,
    var email: String? = null,
    var role: Role? = null,
    private var attributes: Map<String, Any>? = null,
    var githubId: String? = null,
    var avatarUrl: String? = null,
) : OAuth2User,
    UserDetails {
    constructor(name: String?, attribute: Map<String, Any>?) : this()

    override fun getAuthorities(): Collection<GrantedAuthority> = role?.let { listOf(SimpleGrantedAuthority(it.name)) } ?: emptyList()

    override fun getPassword(): String? = null

    override fun getUsername(): String = id?.toString() ?: ""

    // OAuth2User interface methods
    override fun getName(): String? = name

    override fun getAttributes(): Map<String, Any> = attributes ?: emptyMap()

    companion object {
        fun from(member: Member): MemberDetails =
            MemberDetails(
                id = member.id,
                name = member.name,
                email = member.email,
                role = member.role,
            )
    }
}
