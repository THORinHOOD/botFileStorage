package com.benchinc.benchBot.services

import com.db.benchLib.data.Bench
import com.pengrad.telegrambot.model.Location

interface GeoService {
    fun findBenchById(id: String) : Bench
    fun findBenches(around: Location, radius: Int) : List<Bench>
}