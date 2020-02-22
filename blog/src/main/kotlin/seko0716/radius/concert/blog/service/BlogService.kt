package seko0716.radius.concert.blog.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.blog.domain.Blog
import seko0716.radius.concert.blog.repository.BlogRepository
import seko0716.radius.concert.security.domains.User


@Service
class BlogService constructor(
    private val blogRepository: BlogRepository
) {
    fun createBlog(
        author: User,
        title: String,
        preview: String,
        content: String
    ): Mono<Blog> {
        return saveBlog(
            Blog(
                title = title,
                content = content,
                author = author,
                preview = preview
            )
        )
    }

    fun createBlog(
        author: User,
        title: String,
        content: String,
        preview: String,
        event: String?
    ): Mono<Blog> {
        return saveBlog(
            Blog(
                title = title,
                content = content,
                author = author,
                event = event,
                preview = preview
            )
        )
    }

    fun saveBlog(blog: Blog): Mono<Blog> {
        return blogRepository.save(blog)
    }

    fun updateBlog(
        objectId: ObjectId,
        title: String?,
        preview: String?,
        content: String?,
        event: String?
    ): Mono<Blog> {
        return blogRepository.findById(objectId)
            .map {
                Blog(
                    id = it.id,
                    title = title ?: it.title,
                    content = content ?: it.content,
                    author = it.author,
                    event = event,
                    createdWhen = it.createdWhen,
                    preview = preview ?: it.preview
                )
            }
            .flatMap { saveBlog(it) }
    }

    fun updateBlog(
        objectId: String,
        title: String?,
        preview: String?,
        content: String?,
        event: String?
    ): Mono<Blog> {
        return updateBlog(ObjectId(objectId), title, preview, content, event)
    }

    fun getBlogs(): Flux<Blog> {
        return blogRepository.findAll()
    }

    fun getBlogById(objectId: ObjectId): Mono<Blog> {
        return blogRepository.findById(objectId)
    }

    fun getBlogById(objectId: String): Mono<Blog> {
        return getBlogById(ObjectId(objectId))
    }

    fun getBlogs(n: Int): Flux<Blog> {
        return blogRepository.findTop50By().take(n.toLong())
    }
}