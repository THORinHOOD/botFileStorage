package com.geokittens.benchBot.services.bot.pagination

import com.thorinhood.benchLib.proto.bench.BenchInfo
import com.thorinhood.benchLib.proto.bench.BenchInfoWithDistance

interface BenchInfoStrategy {
    fun description(benchInfo: BenchInfo): String
    fun description(benchInfoWithDistance: BenchInfoWithDistance): String
}