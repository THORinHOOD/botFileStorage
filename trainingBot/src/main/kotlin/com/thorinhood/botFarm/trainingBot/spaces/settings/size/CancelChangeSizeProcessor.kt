package com.thorinhood.botFarm.trainingBot.spaces.settings.size

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.KeyboardMarkups

@Processor
class CancelChangeSizeProcessor : BaseCancelProcessor(
    "cancel_change_size",
    "change_size",
    Transition(
        "default",
       "Окей, не будем",
       KeyboardMarkups.DEFAULT_KEYBOARD
    )
)