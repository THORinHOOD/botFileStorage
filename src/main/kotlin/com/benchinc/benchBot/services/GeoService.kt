package com.benchinc.benchBot.services

import com.benchinc.benchBot.data.Bench
import com.pengrad.telegrambot.model.Location
import de.westnordost.osmapi.map.data.Node

interface GeoService {
    fun findBenchById(id: Long) : Node?
    fun findBenches(around : Location, radius: Int) : List<Bench>
}