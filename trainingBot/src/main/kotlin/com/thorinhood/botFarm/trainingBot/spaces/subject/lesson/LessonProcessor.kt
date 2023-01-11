package com.thorinhood.botFarm.trainingBot.spaces.subject.lesson

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import com.thorinhood.botFarm.engine.processors.BaseProcessor
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.data.ProcessResult
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.trainingBot.domain.Lesson
import com.thorinhood.botFarm.trainingBot.services.LessonService
import com.thorinhood.botFarm.trainingBot.statics.ArgKey
import com.thorinhood.botFarm.trainingBot.statics.Emojis
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class LessonProcessor(
    private val lessonService: LessonService
) : BaseProcessor(
    "lesson", ProcSpace.LESSON
) {
    override fun processInner(session: Session, update: Update): ProcessResult {
        val lesson : Lesson = session[ArgKey.LESSON]
        if (lesson.getCurrentTask().answers.contains(update.message()?.text()?.lowercase())) {
            val previousTask = lesson.removeCurrentTask()
            if (lesson.hasTask()) {
                val nextTaskMessage = lessonService.makeCurrentTaskMessage(session.sessionId, lesson)
                return ProcessResult(
                    listOf(
                        SendMessage(
                            session.sessionId, "Правильно! ${Emojis.SUNGLASSES}" +
                                    "\n${previousTask.question} - ${previousTask.answers.joinToString("; ")}"
                        ),
                        nextTaskMessage
                    )
                )
            } else {
                session.remove(ArgKey.LESSON)
                return ProcessResult(
                    null,
                    Transition(
                        ProcSpace.DEFAULT,
                        "Правильно! ${Emojis.SUNGLASSES}" +
                                "\n${previousTask.question} - ${previousTask.answers.joinToString("; ")}" +
                                "\nТы молодец! ${Emojis.CLAP}" +
                                "\nДо следующего занятия!",
                        KeyboardMarkups.DEFAULT_KEYBOARD
                    ) { innerSession -> innerSession.remove(ArgKey.SELECTED_SUBJECT) }
                )
            }
        } else {
            return ProcessResult(listOf(
                SendMessage(
                    session.sessionId,
                    "${Emojis.HMM}\nХмм, неправильно, попробуй снова"
                )
            ))
        }
    }

    override fun isThisProcessorInner(session: Session, update: Update): Boolean =
        !isUpdateMessageEqualsLabel(update, "Закончить занятие") &&
                !isUpdateMessageEqualsLabel(update, "Не знаю")
}