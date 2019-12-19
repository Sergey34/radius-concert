package seko0716.radius.concert.view.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class ViewController {
    @RequestMapping("/")
    fun root(model: Model): String {
        return "index"
    }

    @GetMapping("/events/{currentCity}/{distance}/{metric}")
    fun events(model: Model): String {
        return "events"
    }
}