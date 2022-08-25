package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.pengrad.telegrambot.model.Update
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
class InputSubjectGoogleTableSheetProcessor : BaseProcessor(
    "input_google_table_sheet",
    ProcSpace.INPUT_SUBJECT_GOOGLE_TABLE_SHEET
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val googleTableSheet = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        val builder = (session.args[ArgKey.SUBJECT_BUILDER] as Subject.Builder).googleTableSheet(googleTableSheet)
        @Suppress("UNCHECKED_CAST")
        val allSubjects = session.args.getOrPut(ArgKey.SUBJECTS) { mutableMapOf<String, Subject>() } as AllSubjects
        allSubjects[builder.name] = Subject(builder, 60)
        session.args.remove(ArgKey.SUBJECT_BUILDER)
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