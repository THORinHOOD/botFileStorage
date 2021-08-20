package com.benchinc.benchBot.data

import com.benchinc.benchBot.geo.Bench

class AllSessions {

    private val sessions: MutableMap<Long, Session> = mutableMapOf()

    fun setBenches(chatId : Long, benches : List<Bench>) {
        getSession(chatId).currentBenches = benches
    }

    fun getBenches(chatId : Long) : List<Bench> = getSession(chatId).currentBenches

//    fun addMessagePage(chatId: Long, messageId: Int, pageIndex: Int) {
//        getSession(chatId).messagePage[messageId] = pageIndex
//    }
//
//    fun getMessagePage(chatId: Long, messageId: Int) : Int? = getSession(chatId).messagePage[messageId]

    fun setRadius(chatId: Long, radius: Int) {
        getSession(chatId).radius = radius
    }

    fun getRadius(chatId: Long) : Int = getSession(chatId).radius

    fun getSession(chatId : Long) : Session {
        if (sessions[chatId] == null) {
            sessions[chatId] = Session(chatId, listOf(), 500)
        }
        return sessions[chatId] ?: throw Exception("something goes wrong")
    }
}