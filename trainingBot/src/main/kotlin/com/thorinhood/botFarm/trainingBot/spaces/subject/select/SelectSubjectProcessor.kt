package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.data.services.SessionArgumentsDataService
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Session
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class SelectSubjectProcessor(
    private val sessionArgumentsDataService: SessionArgumentsDataService
) : BaseProcessor(
    "select_subject",
    ProcSpace.SELECT_SUBJECT
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        val subjectName = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        return sessionArgumentsDataService.maintainWrap(session.sessionId) { args ->
            if (!args.containsKey(ArgKey.SUBJECTS)) {
                return@maintainWrap ProcessResult(
                    null,
                    Transition(
                        ProcSpace.DEFAULT,
                        "${Emojis.HMM}\nНе найдено ни одного предмета",
                        KeyboardMarkups.DEFAULT_KEYBOARD
                    )
                )
            }
            val subjects = args.get<AllSubjects>(ArgKey.SUBJECTS)
            if (!subjects.containsKey(subjectName)) {
                return@maintainWrap ProcessResult(
                    null,
                    Transition(
                        ProcSpace.SELECT_SUBJECT,
                        "Такого предмета нет, попробуй снова"
                    )
                )
            }
            args[ArgKey.SELECTED_SUBJECT] = subjectName
            return@maintainWrap ProcessResult(
                null,
                Transition(
                    ProcSpace.IN_SUBJECT,
                    "Что будем делать с этим предметом?",
                    KeyboardMarkups.SUBJECT_KEYBOARD
                )
            )
        }
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)
}