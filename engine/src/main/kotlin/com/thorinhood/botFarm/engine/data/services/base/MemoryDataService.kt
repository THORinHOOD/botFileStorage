package com.thorinhood.botFarm.engine.data.services.base

import com.thorinhood.botFarm.engine.data.entities.Entity
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class MemoryDataService : DataService {

    private val allData: MutableMap<Long, Entity> = mutableMapOf()

    override fun <T: Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>, default: () -> T): T =
        getOneBySessionId(sessionId, clazz) ?: default.invoke()

    override fun <T: Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>): T? =
        allData[sessionId] as? T

    override fun <T : Entity> getAll(clazz: KClass<T>): List<T> {
        return allData.values as List<T>
    }

    override fun <T: Entity> update(data: T) : T {
        allData[data.sessionId] = data as Entity
        return data
    }

    override fun <T: Entity> delete(data: T) {
        allData.remove(data.sessionId)
    }
}
