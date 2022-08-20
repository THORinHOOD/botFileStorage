package com.thorinhood.botFarm.trainingBot.spaces.settings.interval

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
class StartChangeIntervalProcessor : BaseProcessor(
    "start_change_interval",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult =
        ProcessResult(
            null,
            Transition(
                ProcSpace.CHANGE_INTERVAL,
                "Напиши, как часто надо приходить к тебе с заданиями (каждые N-минут).\n" +
                        "На данный момент я прихожу к тебе " +
                        "каждые ${(session.args[ArgKey.TIMER_CONFIG] as TimerConfig).interval} минут",
                KeyboardMarkups.CANCEL_KEYBOARD
            )
        )

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Изменить интервал выдачи заданий")
}