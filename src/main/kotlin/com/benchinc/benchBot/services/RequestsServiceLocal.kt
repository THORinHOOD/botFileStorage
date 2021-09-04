package com.benchinc.benchBot.services

import com.benchinc.benchBot.data.Request
import com.db.benchLib.data.PointGeo
import com.pengrad.telegrambot.model.Location
import org.springframework.stereotype.Service

@Service
class RequestsServiceLocal {

    private val requests: MutableMap<Long, Request> = mutableMapOf()

    fun addRequest(chatId: Long, location: Location) {
        requests[chatId] = Request(chatId, PointGeo("Point", listOf(
            location.longitude().toDouble(),
            location.latitude().toDouble()
        )))
    }

    fun getRequest(chatId: Long) : Request {
        if (!requests.containsKey(chatId)) {
            throw IllegalArgumentException("Can't find $chatId request")
        }
        return requests[chatId]!!
    }

}