package seko0716.radius.concert.security.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import seko0716.radius.concert.security.domains.User


@Controller
class T {
    @GetMapping("/ttt")
    fun index(
        model: Model,
        @AuthenticationPrincipal oauth2User: User?
    ): String {
        return "about"
    }
}