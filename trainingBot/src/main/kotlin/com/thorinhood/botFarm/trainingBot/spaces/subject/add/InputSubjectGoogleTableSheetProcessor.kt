package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.ScheduleConfig
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class InputSubjectGoogleTableSheetProcessor(
    private val subjectService: SubjectService
) : BaseProcessor(
    "input_google_table_sheet",
    ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_SHEET
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val googleTableSheet = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        val builder = session.get<Subject.Builder>(ArgKey.SUBJECT_BUILDER).googleTableSheet(googleTableSheet)
        val allSubjects = session.getOrPut(ArgKey.SUBJECTS) { mutableMapOf<String, Subject>() }
        val subject = builder.scheduleConfig(
            ScheduleConfig(
                "subject_${builder.name}_${session.sessionId}",
                1 * 60 * 60 * 1000
            )
        ).build()
        allSubjects[subject.name] = subject
        session.remove(ArgKey.SUBJECT_BUILDER)
        subjectService.scheduleSubject(session, subject)
        return ProcessResult(
            null,
            Transition(
                ProcSpace.DEFAULT,
                "Готово!\n" +
                        "Добавлен новый предмет!",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}