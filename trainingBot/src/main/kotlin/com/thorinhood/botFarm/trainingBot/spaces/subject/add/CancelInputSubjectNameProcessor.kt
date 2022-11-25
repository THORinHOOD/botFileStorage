package com.thorinhood.botFarm.trainingBot.spaces.subject.add

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelInputSubjectNameProcessor : BaseCancelProcessor(
    "cancel_input_subject_name",
    ProcSpace.INPUT_SUBJECT_NAME,
    Transition(
        ProcSpace.DEFAULT,
        "Окей, не будем",
        KeyboardMarkups.SUBJECT_KEYBOARD
    )
)