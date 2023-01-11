package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class InputSubjectNameProcessor : BaseProcessor(
    "input_subject_name",
    ProcSpace.INPUT_SUBJECT_NAME
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        val name = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        session[ArgKey.SUBJECT_BUILDER] = Subject.Builder().name(name)
        return ProcessResult(
            null,
            Transition(
                ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_ID,
                "Отлично!\nА теперь введи id google таблицы"
            )
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update)

}