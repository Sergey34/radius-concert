package seko0716.radius.concert.view.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class ViewController {
    @RequestMapping(path = ["/", "/events"])
    fun root(model: Model): String {
        return "index"
    }

    @GetMapping("/events/{currentCity}/{distance}/{metric}")
    fun events(model: Model): String {
        return "events"
    }

    @GetMapping("/blog")
    fun blog(model: Model): String {
        return "blog"
    }

    @GetMapping("/blog/{id}")
    fun blogSingle(model: Model): String {
        return "blog-single"
    }

    @GetMapping("/contact")
    fun contact(model: Model): String {
        return "contact"
    }

    @GetMapping("/about")
    fun about(model: Model): String {
        return "about"
    }
}