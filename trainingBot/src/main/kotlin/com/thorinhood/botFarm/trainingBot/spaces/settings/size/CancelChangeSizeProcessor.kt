package com.thorinhood.botFarm.trainingBot.spaces.settings.size

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelChangeSizeProcessor : BaseCancelProcessor(
    "cancel_change_size",
    ProcSpace.CHANGE_SIZE,
    Transition(
        ProcSpace.DEFAULT,
       "Окей, не будем",
       KeyboardMarkups.DEFAULT_KEYBOARD
    )
)