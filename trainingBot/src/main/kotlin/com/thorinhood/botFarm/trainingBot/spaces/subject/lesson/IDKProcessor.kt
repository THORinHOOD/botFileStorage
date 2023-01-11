package com.thorinhood.botFarm.trainingBot.spaces.subject.lesson

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class IDKProcessor : BaseProcessor(
    "lesson_idk",
    ProcSpace.LESSON
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        val lesson = session.args[ArgKey.LESSON] as Lesson
        val answer = (lesson.getCurrentTask().answers).first()
        val blank = answer.indices.random()
        val hint = answer.mapIndexed { i, symbol -> if (i == blank) "_" else symbol }.joinToString()
        return ProcessResult(
            listOf(SendMessage(
                session.sessionId,
                "Подсказка : \n\"${hint}\""
            ))
        )
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        isNotCancel(update) && isUpdateMessageEqualsLabel(update, "Не знаю")
}