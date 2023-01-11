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
class StartChangeSizeProcessor : BaseProcessor(
    "start_change_size",
    ProcSpace.IN_SUBJECT
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        @Suppress("UNCHECKED_CAST")
        val subjects = session.args[ArgKey.SUBJECTS] as AllSubjects
        return ProcessResult(
            null,
            Transition(
                ProcSpace.CHANGE_SIZE,
                "Напиши, сколько вопросов должно быть в одном задании.\n" +
                        "На данный момент я прихожу к тебе с " +
                        "${subjects[session.args[ArgKey.SELECTED_SUBJECT]]!!.lessonSize} вопросами",
                KeyboardMarkups.CANCEL_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Изменить кол-во заданий в выдаче")
}