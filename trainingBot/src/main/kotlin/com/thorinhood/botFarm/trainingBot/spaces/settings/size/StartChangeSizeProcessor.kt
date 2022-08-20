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
class StartChangeSizeProcessor : BaseProcessor(
    "start_change_size",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult =
        ProcessResult(
            null,
            Transition(
                "change_size",
                "Напиши, сколько вопросов должно быть в одном задании.\n" +
                        "На данный момент я прихожу к тебе с " +
                        "${(session.args[ArgKey.TIMER_CONFIG] as TimerConfig).size} вопросами",
                KeyboardMarkups.CANCEL_KEYBOARD
            )
        )

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Изменить кол-во заданий в выдаче")
}