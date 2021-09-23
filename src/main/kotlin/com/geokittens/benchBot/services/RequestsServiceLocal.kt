package com.geokittens.benchBot.services

import com.geokittens.benchBot.data.RequestLocal
import com.db.benchLib.data.PointGeo
import com.pengrad.telegrambot.model.Location
import org.springframework.stereotype.Service

@Service
class RequestsServiceLocal {

    private val requests: MutableMap<Long, RequestLocal> = mutableMapOf()

    fun addRequest(chatId: Long, location: Location) {
        requests[chatId] = RequestLocal(chatId, PointGeo("Point", listOf(
            location.longitude().toDouble(),
            location.latitude().toDouble()
        )))
    }

    fun getRequest(chatId: Long) : RequestLocal {
        if (!requests.containsKey(chatId)) {
            throw IllegalArgumentException("Can't find $chatId request")
        }
        return requests[chatId]!!
    }

}