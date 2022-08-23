package com.thorinhood.botFarm.trainingBot.domain

import java.sql.Timestamp

data class TimerConfig(
    var interval: Long,
    var from: Timestamp,
    var to: Timestamp
) {

    fun changeInterval(interval: Long) {
        this.interval = interval
        val currentMillis = System.currentTimeMillis()
        from.time = currentMillis
        to.time = currentMillis + interval * 60 * 1000
    }

    fun checkAndUpdate(currentTimestamp: Timestamp) : Boolean {
        return if (currentTimestamp.after(to) || currentTimestamp.equals(to)) {
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
                Timestamp(currentMillis),
                Timestamp(currentMillis + interval * 60 * 1000)
            )
        }
    }


}