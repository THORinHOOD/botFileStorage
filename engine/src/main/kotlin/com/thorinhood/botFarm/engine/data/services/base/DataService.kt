package com.thorinhood.botFarm.engine.data.services.base

import com.thorinhood.botFarm.engine.data.entities.Entity
import kotlin.reflect.KClass

interface DataService {
    fun <T : Entity, R> maintainWrap(sessionId: Long, clazz: KClass<T>, default: () -> T, process: (T) -> R) : R =
        getOneBySessionId(sessionId, clazz, default).let { entity ->
            val result = process(entity)
            update(entity)
            result
        }
    fun <T : Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>) : T?
    fun <T : Entity> getOneBySessionId(sessionId: Long, clazz: KClass<T>, default: () -> T) : T
    fun <T : Entity> getAll(clazz: KClass<T>) : List<T>
    fun <T : Entity> update(data: T) : T
    fun <T : Entity> delete(data: T)
}
