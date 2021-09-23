package com.geokittens.benchBot.services.bot.description

import com.thorinhood.benchLib.proto.bench.BenchInfo
import com.thorinhood.benchLib.proto.bench.BenchInfoWithDistance

interface BenchDescriptionStrategy {
    fun description(benchInfo: BenchInfo): String
    fun description(benchInfoWithDistance: BenchInfoWithDistance): String
}