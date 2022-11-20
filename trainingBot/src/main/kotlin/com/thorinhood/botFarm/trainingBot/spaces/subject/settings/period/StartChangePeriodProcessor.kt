package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.period

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
class StartChangePeriodProcessor : BaseProcessor(
    "start_change_period",
    ProcSpace.IN_SUBJECT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val subjects = session.get<AllSubjects>(ArgKey.SUBJECTS)
        val milliseconds = subjects[session[ArgKey.SELECTED_SUBJECT]]!!.scheduleConfig.period
        return ProcessResult(
            null,
            Transition(
                ProcSpace.CHANGE_PERIOD,
                "Напиши, как часто надо приходить к тебе с заданиями (каждые N-минут).\n" +
                        "На данный момент я прихожу к тебе " +
                        "каждые ${milliseconds/1000/60} минут",
                KeyboardMarkups.CANCEL_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Изменить интервал выдачи заданий")
}