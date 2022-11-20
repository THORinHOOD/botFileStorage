package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.interval

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class ChangePeriodProcessor(
    private val subjectService: SubjectService
) : BaseProcessor(
    "change_period",
    ProcSpace.CHANGE_PERIOD
) {

    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val newPeriod = update.message()?.text()?.toLong() ?: throw Exception("Попробуй ещё раз")
        val subjects = session.get<AllSubjects>(ArgKey.SUBJECTS)
        val subject = subjects[session[ArgKey.SELECTED_SUBJECT]]!!
        subject.scheduleConfig.period = newPeriod * 60 * 1000
        subjectService.rescheduleSubject(session, subject)
        return ProcessResult(
            null,
            Transition(
                ProcSpace.IN_SUBJECT,
                "Поменял интервал на каждые $newPeriod минут",
                KeyboardMarkups.SUBJECT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}