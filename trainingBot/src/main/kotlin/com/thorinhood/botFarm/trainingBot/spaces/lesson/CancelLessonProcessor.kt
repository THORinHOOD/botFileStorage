package com.thorinhood.botFarm.trainingBot.spaces.lesson

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelLessonProcessor : BaseCancelProcessor(
    "cancel_lesson",
    ProcSpace.LESSON,
    Transition(
        ProcSpace.DEFAULT,
        "Okaaaay, let's stop!",
        KeyboardMarkups.DEFAULT_KEYBOARD
    ) { session -> session.cursor.args.remove("answers") }
)