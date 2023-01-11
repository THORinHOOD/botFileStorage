package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.AllSubjects
import com.thorinhood.botFarm.trainingBot.domain.Subject
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class StartSelectSubjectProcessor : BaseProcessor(
    "start_select_subject",
    ProcSpace.DEFAULT
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        @Suppress("UNCHECKED_CAST")
        val subjects = session.args.getOrDefault(ArgKey.SUBJECTS, mutableMapOf<String, Subject>()) as AllSubjects
        val buttons = subjects.keys.map { subject ->
            arrayOf(KeyboardButton(subject))
        }.toMutableList()
        if (buttons.isEmpty()) {
            return ProcessResult(
                null,
                Transition(
                    ProcSpace.DEFAULT,
                    "Не найдено ни одного предмета",
                    KeyboardMarkups.DEFAULT_KEYBOARD
                )
            )
        }
        buttons.add(arrayOf(KeyboardButton("Отмена")))
        return ProcessResult(
            null,
            Transition(
                ProcSpace.SELECT_SUBJECT,
                "Выбери один из предметов",
                ReplyKeyboardMarkup(*buttons.toTypedArray())
            )
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Перейти к предмету")

}