package seko0716.radius.concert.security.domains

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class DtoUser(
    val login: String?,
    val pwd: String?,
    val cpwd: String?,
    @NotBlank
    val firstName: String = "",
    @NotBlank
    val lastName: String = "",
    @field:Email
    val email: String?
) {
    fun isValid() = pwd == cpwd
}