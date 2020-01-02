package seko0716.radius.concert.security.controller

import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.domains.User
import seko0716.radius.concert.security.service.MongoUserDetailService


@Controller
class T constructor(
    private val mongoUserDetailService: MongoUserDetailService
) {
    @GetMapping("/ttt")
    @PreAuthorize("@mongoUserDetailService.enabledOrNull(#user)")
    fun index(
        model: Model,
        @AuthenticationPrincipal user: User?
    ): Mono<String> {
        return Mono.just("about")
    }

    @GetMapping("/fill_password_page")
    fun fillPasswordPage(
        model: Model,
        @AuthenticationPrincipal user: User
    ): Mono<String> {
        model.addAttribute("body", Password(""))
        return Mono.just("fill_password_page")
    }

    @PostMapping("/fill_password_page", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun fillPassword(
        @ModelAttribute password: Password,
        model: Model,
        @AuthenticationPrincipal user: User,
        @AuthenticationPrincipal authentication: Authentication
    ): Mono<String> {
        return mongoUserDetailService.updatePassword(user, password)
            .map { "fill_password_page" }
    }
}

data class Password(
    val psw: String = ""
)
