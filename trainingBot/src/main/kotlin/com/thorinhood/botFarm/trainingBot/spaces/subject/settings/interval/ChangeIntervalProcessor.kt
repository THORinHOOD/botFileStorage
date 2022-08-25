package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.interval

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class ChangeIntervalProcessor : BaseProcessor(
    "change_interval",
    ProcSpace.CHANGE_INTERVAL
) {

    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val newInterval = update.message()?.text()?.toLong() ?: throw Exception("Попробуй ещё раз")
        @Suppress("UNCHECKED_CAST")
        val subjects = session.args[ArgKey.SUBJECTS] as AllSubjects
        subjects[session.args[ArgKey.SELECTED_SUBJECT]]!!.timerConfig.changeInterval(newInterval)
        return ProcessResult(
            null,
            Transition(
                ProcSpace.IN_SUBJECT,
                "Поменял интервал на каждые $newInterval минут",
                KeyboardMarkups.SUBJECT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}