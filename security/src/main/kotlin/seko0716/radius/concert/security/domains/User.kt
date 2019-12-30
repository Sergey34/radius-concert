package seko0716.radius.concert.security.domains

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
    @Id
    val id: ObjectId = ObjectId(),
    val socialAccountId: String,
    val login: String,
    val pass: String,
    val email: String? = null,
    val firstName: String,
    val lastName: String? = null,
    val enabled: Boolean = true,
    val roles: List<Role> = listOf(),
    val authServiceType: String = "BASE"
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = roles

    override fun isEnabled(): Boolean = enabled

    override fun getUsername(): String = login

    override fun getPassword(): String = pass

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}
