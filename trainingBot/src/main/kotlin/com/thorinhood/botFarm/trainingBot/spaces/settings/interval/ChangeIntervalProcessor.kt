package com.thorinhood.botFarm.trainingBot.spaces.settings.interval

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig

@Processor
class ChangeIntervalProcessor : BaseProcessor(
    "change_interval",
    "change_interval"
) {

    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val newInterval = update.message()?.text()?.toInt() ?: throw Exception("Попробуй ещё раз")
        val timerConfig = session.args["timer_config"] as TimerConfig
        timerConfig.interval = newInterval
        return ProcessResult(
            null,
            Transition(
                "default",
                "Поменял интервал на каждые ${newInterval} минут",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}