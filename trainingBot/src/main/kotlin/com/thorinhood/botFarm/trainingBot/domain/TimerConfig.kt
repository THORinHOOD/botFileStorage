package com.thorinhood.botFarm.trainingBot.domain

import java.util.Date

data class TimerConfig(
    var interval: Long,
    var from: Date,
    var to: Date
) {
    constructor(now: Date) : this(interval = 60, from = now, to = Date(now.time + 60))

    fun changeInterval(interval: Long) {
        this.interval = interval
        val currentMillis = System.currentTimeMillis()
        from.time = currentMillis
        to.time = currentMillis + interval * 60 * 1000
    }

    fun checkAndUpdate(currentTimestamp: Date) : Boolean {
        return if (currentTimestamp.after(to) || currentTimestamp == to) {
            from.time = to.time
            to.time = from.time + interval * 60 * 1000
            true
        } else {
            false
        }
    }

    companion object {
        fun makeNew(interval: Long) : TimerConfig {
            val currentMillis = System.currentTimeMillis()
            return TimerConfig(
                interval,
                Date(currentMillis),
                Date(currentMillis + interval * 60 * 1000)
            )
        }
    }


}