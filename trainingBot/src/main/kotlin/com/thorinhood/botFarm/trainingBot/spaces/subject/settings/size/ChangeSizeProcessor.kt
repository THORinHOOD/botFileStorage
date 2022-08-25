package com.thorinhood.botFarm.trainingBot.spaces.subject.settings.size

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class ChangeSizeProcessor : BaseProcessor(
    "change_size",
    ProcSpace.CHANGE_SIZE
) {

    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val newSize = update.message()?.text()?.toInt() ?: throw Exception("Попробуй ещё раз")
        @Suppress("UNCHECKED_CAST")
        val subjects = session.args[ArgKey.SUBJECTS] as AllSubjects
        subjects[session.args[ArgKey.SELECTED_SUBJECT]]!!.lessonSize = newSize
        return ProcessResult(
            null,
            Transition(
                ProcSpace.IN_SUBJECT,
                "Теперь в каждом занятии будет $newSize вопросов",
                KeyboardMarkups.SUBJECT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}