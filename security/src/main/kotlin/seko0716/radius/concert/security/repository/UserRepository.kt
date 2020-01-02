package seko0716.radius.concert.security.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seko0716.radius.concert.security.domains.User

@Repository
interface UserRepository : ReactiveMongoRepository<User, ObjectId> {
    fun findUserByLogin(username: String): Mono<User>
}