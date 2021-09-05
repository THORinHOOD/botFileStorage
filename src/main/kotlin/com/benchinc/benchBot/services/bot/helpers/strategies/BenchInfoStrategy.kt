package com.benchinc.benchBot.services.bot.helpers.strategies

import com.db.benchLib.data.bench.BenchDto
import com.db.benchLib.data.bench.BenchInfoWithDistance

interface BenchInfoStrategy {
    fun description(benchDto: BenchDto) : String
    fun description(benchInfoWithDistance: BenchInfoWithDistance) : String
}