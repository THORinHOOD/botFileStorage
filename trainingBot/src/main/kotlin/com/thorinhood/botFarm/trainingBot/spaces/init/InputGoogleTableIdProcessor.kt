package com.thorinhood.botFarm.trainingBot.spaces.init

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class InputGoogleTableIdProcessor : BaseProcessor(
    "input_google_table_id",
    "input_google_table_id"
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        session.args["google_table_id"] = update.message()?.text() ?: throw Exception("Попробуй ещё раз")
        return ProcessResult(
            null,
            Transition(
                "input_google_table_sheet",
                "Отлично!\nА теперь пришли названия листа в таблице"
            )
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)
}