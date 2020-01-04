package seko0716.radius.concert.security.domains

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

@Document
data class User(
    @Id
    val id: ObjectId = ObjectId(),
    val socialAccountId: String? = null,
    val login: String,
    var pass: String,
    var email: String? = null,
    var firstName: String,
    var lastName: String? = null,
    var enabled: Boolean = true,
    val roles: List<GrantedAuthority> = listOf(),
    val authServiceType: String = "BASE",
    val attrs: Map<String, Any> = mapOf()
) : UserDetails, OAuth2User {

    override fun getAuthorities(): Collection<GrantedAuthority> = roles

    override fun isEnabled(): Boolean = enabled

    override fun getUsername(): String = login

    override fun getPassword(): String = pass

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun getName(): String = login

    override fun getAttributes(): Map<String, Any> = attrs

}
