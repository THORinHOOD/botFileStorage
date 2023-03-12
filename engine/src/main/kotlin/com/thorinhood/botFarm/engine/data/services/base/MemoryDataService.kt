package com.thorinhood.botFarm.engine.data.services.base

import com.thorinhood.botFarm.engine.data.entities.Entity
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class MemoryDataService : DataService {

    private val allData: MutableMap<Long, MutableMap<KClass<*>, in Entity>> = mutableMapOf()
    override fun <T : Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>): T? =
        allData[sessionId]?.get(clazz) as? T

    override fun <T : Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>, default: () -> T): T =
        getOneBySessionId(sessionId, clazz) ?: default.invoke()

    override fun <T : Entity> getAll(clazz: KClass<T>): List<T> =
        allData.values.flatMap { it.values } as List<T>

    override fun <T: Entity> update(data: T) : T {
        allData.putIfAbsent(data.sessionId, mutableMapOf())
        allData[data.sessionId]!![data::class] = data as Entity
        return data
    }

    override fun <T: Entity> delete(data: T) {
        allData[data.sessionId]?.remove(data::class)
    }
}
