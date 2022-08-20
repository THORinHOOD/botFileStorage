package com.thorinhood.botFarm.trainingBot.spaces.init

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
import com.thorinhood.botFarm.trainingBot.services.GoogleTableService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class InputGoogleTableSheetProcessor(
    private val googleTableService: GoogleTableService
) : BaseProcessor(
    "input_google_table_sheet",
    ProcSpace.INPUT_GOOGLE_TABLE_SHEET
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        session.args[ArgKey.GOOGLE_TABLE_ID] = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        session.args[ArgKey.TIMER_CONFIG] = TimerConfig(
            5,
            5,
            0
        )
        return ProcessResult(
            null,
            Transition(
                ProcSpace.DEFAULT,
                "Готово!",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}