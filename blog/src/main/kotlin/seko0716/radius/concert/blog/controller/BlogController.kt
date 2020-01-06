package seko0716.radius.concert.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import seko0716.radius.concert.blog.domain.Blog
import seko0716.radius.concert.blog.service.BlogService

@RestController
@RequestMapping("/api/blogs")
class BlogController @Autowired constructor(
    private val blogService: BlogService
) {
    @GetMapping("/{count}")
    fun getBlogs(@PathVariable(name = "count") n: Int): Flux<Blog> {
        return blogService.getBlogs(n)
    }
}