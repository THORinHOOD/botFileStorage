package com.thorinhood.botFarm.trainingBot.spaces

import com.pengrad.telegrambot.model.Update
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session

@Processor
class InputAnswerProcessor : BaseProcessor(
    "input_answer",
    "input_answer"
) {
    override fun processInner(session: Session<Long>, update: Update): ProcessResult {
        val answers = session.cursor.args["answers"] as List<*>
        return ProcessResult(
            null,
            if (answers.contains(update.message()?.text())) {
                Transition("default", "Правильно!")
            } else {
                Transition("input_answer", "Неправильно, попробуй ещё раз")
            }
        )
    }

    override fun isThisProcessorInner(session: Session<Long>, update: Update): Boolean =
        isNotCancel(update)

}