package seko0716.radius.concert.event.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import seko0716.radius.concert.event.domains.Geocode

@Repository
class GeocodeRepository @Autowired constructor(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val geocodeMongoRepository: GeocodeMongoRepository
) {
    fun saveAll(events: Collection<Geocode>): Flow<Geocode> {
        return geocodeMongoRepository.saveAll(events).asFlow()
    }

    fun getGeocodesByTemplate(template: String): Flow<Geocode> {
        return mongoTemplate.find<Geocode>(
            Query.query(
                Criteria.where("nameForSearch").regex(template)
            )
        ).asFlow()
    }

    fun saveAll(geocodes: List<Geocode>): Flow<Geocode> {
        return mongoTemplate.insertAll<Geocode>(geocodes).asFlow()
    }

    suspend fun find(name: String): Geocode? {
        return mongoTemplate.findById<Geocode>(name).awaitFirstOrNull()
    }

    suspend fun find(names: List<String>): Flow<Geocode> {
        return mongoTemplate.find<Geocode>(Query.query(Criteria.where("nameForSearch").`in`(names))).asFlow()
    }
}

@Repository
interface GeocodeMongoRepository : ReactiveMongoRepository<Geocode, String>