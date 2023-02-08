package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Session
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.scheduling.ScheduleConfig
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.services.SubjectService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class InputSubjectGoogleTableSheetProcessor(
    private val subjectService: SubjectService,
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : BaseProcessor(
    "input_google_table_sheet",
    ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_SHEET
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        val googleTableSheet = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        val subject = sessionArgumentsDataService.maintainWrap(session.sessionId) { args ->
            val builder = args.get<Subject.Builder>(ArgKey.SUBJECT_BUILDER).googleTableSheet(googleTableSheet)
            val allSubjects = args.getOrPut(ArgKey.SUBJECTS) { mutableMapOf<String, Subject>() }
            val subject = builder.scheduleConfig(
                ScheduleConfig(
                    "subject_${builder.name}_${session.sessionId}",
                    1 * 60 * 60 * 1000
                )
            ).build()
            allSubjects[subject.name] = subject
            args.remove(ArgKey.SUBJECT_BUILDER)
            subject
        }
        return ProcessResult(
            null,
            Transition(
                ProcSpace.DEFAULT,
                "Готово!\n" +
                        "Добавлен новый предмет!",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        ) { subjectService.scheduleSubject(it.sessionId, subject) }
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)
}