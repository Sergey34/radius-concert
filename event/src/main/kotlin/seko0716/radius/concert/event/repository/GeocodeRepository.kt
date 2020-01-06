package seko0716.radius.concert.event.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.count
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import seko0716.radius.concert.event.domains.Geocode

@Repository
class GeocodeRepository @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val geocodeMongoRepository: GeocodeMongoRepository
) {
    fun saveAll(events: Collection<Geocode>): Flux<Geocode> {
        return geocodeMongoRepository.saveAll(events)
    }

    fun getGeocodesByTemplate(template: String): Flux<Geocode> {
        return mongoTemplate.find(
            Query.query(
                Criteria.where("nameForSearch").regex(template)
            )
        )
    }

    fun saveAll(geocodes: List<Geocode>): Flux<Geocode> {
        return mongoTemplate.insertAll<Geocode>(geocodes)
    }

    fun find(name: String): Mono<Geocode> {
        val query = Query.query(Criteria.where("nameForSearch").`is`(name))
        return mongoTemplate.count<Geocode>(query)
            .flatMap { count ->
                if (count != 1L) {
                    Mono.empty()
                } else {
                    mongoTemplate.find<Geocode>(query).last()
                }
            }
    }

    fun find(names: List<String>): Flux<Geocode> {
        return mongoTemplate.find(Query.query(Criteria.where("nameForSearch").`in`(names)))
    }

    fun findById(name: String): Mono<Geocode> {
        return mongoTemplate.findById<Geocode>(name).defaultIfEmpty(Geocode.EMPTY_GEOCODE)
    }
}

@Repository
interface GeocodeMongoRepository : ReactiveMongoRepository<Geocode, String>