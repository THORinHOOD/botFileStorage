package com.thorinhood.botFarm.engine.data.services.base

import com.thorinhood.botFarm.engine.data.entities.Entity
import org.springframework.data.mongodb.core.MongoTemplate
import kotlin.reflect.KClass

class MongoDataService(
    private val mongoTemplate: MongoTemplate
) : DataService {

    override fun <T: Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>, default: () -> T): T =
        getOneBySessionId(sessionId, clazz) ?: default.invoke()

    override fun <T: Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>): T? =
        mongoTemplate.findById(sessionId, clazz.java)

    override fun <T : Entity> getAll(clazz: KClass<T>): List<T> =
        mongoTemplate.findAll(clazz.java)

    override fun <T: Entity> delete(data: T) {
        mongoTemplate.remove(data)
    }

    override fun <T: Entity> update(data: T) = mongoTemplate.save(data)
}