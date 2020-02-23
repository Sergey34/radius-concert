package seko0716.radius.concert.blog.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.blog.domain.Blog
import seko0716.radius.concert.blog.service.BlogService

@RestController
@RequestMapping("/api")
class BlogController @Autowired constructor(
    private val blogService: BlogService
) {
    @GetMapping("/blogs/{count}")
    fun getBlogs(@PathVariable(name = "count") n: Int): Flux<Blog> {
        return blogService.getBlogs(n)
    }

    @GetMapping("/blogs")
    fun getAllBlogs(): Flux<Blog> {
        return blogService.getBlogs()
    }

    @GetMapping("/blog/{id}")
    fun getBlog(@PathVariable(name = "id") id: String): Mono<Blog> {
        return blogService.getBlog(id)
    }

    @PostMapping("/blogs")
    fun createBlog(@RequestBody blog: Blog): Mono<Blog> {
        return blogService.saveBlog(blog)
    }
}