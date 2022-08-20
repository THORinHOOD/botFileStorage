package com.thorinhood.botFarm.trainingBot.spaces.init

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.domain.TimerConfig
import com.thorinhood.botFarm.trainingBot.services.GoogleTableService
import com.thorinhood.botFarm.trainingBot.services.TaskGiverService

@Processor
class InputGoogleTableSheetProcessor(
    private val googleTableService: GoogleTableService
) : BaseProcessor(
    "input_google_table_sheet",
    "input_google_table_sheet"
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        session.args["google_table_sheet"] = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        session.args["timer_config"] = TimerConfig(
            5,
            5,
            0
        )
        return ProcessResult(
            null,
            Transition(
                "default",
                "Готово!",
                KeyboardMarkups.DEFAULT_KEYBOARD
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}