package com.thorinhood.botFarm.trainingBot.spaces.subject.select

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelSelectSubjectProcessor : BaseCancelProcessor(
    "cancel_select_subject",
    ProcSpace.SELECT_SUBJECT,
    Transition(
        ProcSpace.DEFAULT,
        "Окей, не будем",
        KeyboardMarkups.DEFAULT_KEYBOARD
    )
)