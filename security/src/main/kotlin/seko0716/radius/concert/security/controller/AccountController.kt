package seko0716.radius.concert.security.controller

import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.domains.DtoUser
import seko0716.radius.concert.security.domains.User
import seko0716.radius.concert.security.service.MongoUserDetailService


@Controller
class AccountController constructor(
    private val mongoUserDetailService: MongoUserDetailService
) {
    @GetMapping("/login")
    fun login(): String {
        return "redirect:/"
    }

    @PostMapping("/registration")
    fun registration(dtoUser: DtoUser, session: WebSession): Mono<String> {
        val map = mongoUserDetailService.createUser(dtoUser)
        /*.doOnEach {
            val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                it,
                it.password,
                it.authorities
            )
            ReactiveSecurityContextHolder.getContext()
                .map { sc ->
                    sc.authentication = usernamePasswordAuthenticationToken
                    session.attributes.put(SPRING_SECURITY_CONTEXT_KEY, sc)
                }
        }*/
        return map
            .map { "redirect:/account" }
    }

    @GetMapping("/account")
    fun fillPasswordPage(
        model: Model,
        @AuthenticationPrincipal user: User
    ): Mono<String> {
        model.addAttribute("user", user)
        return Mono.just("account")
    }

    @PostMapping("/account", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun fillPassword(
        @ModelAttribute dtoUser: DtoUser,
        @AuthenticationPrincipal user: User
    ): Mono<String> {
        return mongoUserDetailService.updateUser(user, dtoUser)
            .map { "redirect:/account" }
    }
}

