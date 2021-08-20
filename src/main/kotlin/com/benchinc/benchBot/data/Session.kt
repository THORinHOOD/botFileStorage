package com.benchinc.benchBot.data

import com.benchinc.benchBot.geo.Bench

data class Session(var chatId: Long,
                   var currentBenches: List<Bench>,
                   var messagePage: MutableMap<Int, Int>,
                   var radius: Int)