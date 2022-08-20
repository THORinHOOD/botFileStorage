package com.thorinhood.botFarm.trainingBot.spaces.settings.size

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class ChangeSizeProcessor : BaseProcessor(
    "change_size",
    "change_size"
) {

    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val newSize = update.message()?.text()?.toInt() ?: throw Exception("Попробуй ещё раз")
        val timerConfig = session.args[ArgKey.TIMER_CONFIG] as TimerConfig
        timerConfig.size = newSize
        return ProcessResult(
            null,
            Transition(
                ProcSpace.DEFAULT,
                "Поменял кол-во вопросов на ${newSize} в каждом задании",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}