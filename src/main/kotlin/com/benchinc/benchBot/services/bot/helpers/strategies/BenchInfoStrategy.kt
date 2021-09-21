package com.benchinc.benchBot.services.bot.helpers.strategies

import com.thorinhood.benchLib.proto.bench.BenchInfo
import com.thorinhood.benchLib.proto.bench.BenchInfoWithDistance

interface BenchInfoStrategy {
    fun description(benchInfo: BenchInfo): String
    fun description(benchInfoWithDistance: BenchInfoWithDistance): String
}