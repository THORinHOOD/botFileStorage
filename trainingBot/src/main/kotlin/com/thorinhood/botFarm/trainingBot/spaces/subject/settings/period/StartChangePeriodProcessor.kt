package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.period

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Session
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class StartChangePeriodProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : BaseProcessor(
    "start_change_period",
    ProcSpace.IN_SUBJECT
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        val sessionArguments = sessionArgumentsDataService.getBySessionId(session.sessionId)
        val subjects = sessionArguments.get<AllSubjects>(ArgKey.SUBJECTS)
        val subject = subjects[sessionArguments[ArgKey.SELECTED_SUBJECT]]!!
        val milliseconds = subject.scheduleConfig.period
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

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Изменить интервал выдачи заданий")
}