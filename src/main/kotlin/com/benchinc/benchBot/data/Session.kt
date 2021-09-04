package com.benchinc.benchBot.data

import com.db.benchLib.data.Bench

class Session(var chatId: Long,
              var currentBenches: List<Bench>,
              var radius: Int,
              var currentPipelineInfo: PipelineInfo)