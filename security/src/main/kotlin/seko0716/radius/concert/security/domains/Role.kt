package seko0716.radius.concert.security.domains

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority

@Document
data class Role(
    var name: String
) : GrantedAuthority {
    override fun getAuthority(): String {
        return name
    }
}