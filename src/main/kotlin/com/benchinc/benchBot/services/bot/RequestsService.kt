package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.Request
import com.pengrad.telegrambot.model.Location
import org.springframework.stereotype.Service

@Service
class RequestsService {

    private val requests: MutableMap<Long, Request> = mutableMapOf()

    fun addRequest(chatId: Long, location: Location) {
        requests[chatId] = Request(chatId, null, location)
    }

    fun addPhotoToRequest(chatId: Long, photo: ByteArray) {
        if (!requests.containsKey(chatId)) {
            throw IllegalArgumentException("Can't find $chatId request to add photo")
        }
        requests[chatId]!!.photo = photo
    }

    fun getRequest(chatId: Long) : Request {
        if (!requests.containsKey(chatId)) {
            throw IllegalArgumentException("Can't find $chatId request")
        }
        return requests[chatId]!!
    }

}