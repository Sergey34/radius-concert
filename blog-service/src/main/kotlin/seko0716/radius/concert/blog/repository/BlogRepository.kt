package seko0716.radius.concert.blog.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import seko0716.radius.concert.blog.domain.Blog

interface BlogRepository : ReactiveMongoRepository<Blog, ObjectId> {
    fun findTop50By(): Flux<Blog>
}