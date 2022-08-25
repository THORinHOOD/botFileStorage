package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class SelectSubjectProcessor : BaseProcessor(
    "select_subject",
    ProcSpace.SELECT_SUBJECT
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val subjectName = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        if (!session.args.containsKey(ArgKey.SUBJECTS)) {
            return ProcessResult(
                null,
                Transition(
                    ProcSpace.DEFAULT,
                    "${Emojis.HMM}\nНе найдено ни одного предмета",
                    KeyboardMarkups.DEFAULT_KEYBOARD
                )
            )
        }
        @Suppress("UNCHECKED_CAST")
        val subjects = session.args[ArgKey.SUBJECTS] as AllSubjects
        if (!subjects.containsKey(subjectName)) {
            return ProcessResult(
                null,
                Transition(
                    ProcSpace.SELECT_SUBJECT,
                    "Такого предмета нет, попробуй снова"
                )
            )
        }
        session.args[ArgKey.SELECTED_SUBJECT] = subjectName
        return ProcessResult(
            null,
            Transition(
                ProcSpace.IN_SUBJECT,
                "Что будем делать с этим предметом?",
                KeyboardMarkups.SUBJECT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}