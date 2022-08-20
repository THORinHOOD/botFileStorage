package com.thorinhood.botFarm.trainingBot.spaces.settings.interval

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.KeyboardMarkups

@Processor
class CancelChangeIntervalProcessor : BaseCancelProcessor(
    "cancel_change_interval",
    "change_interval",
    Transition(
        "default",
       "Окей, не будем",
       KeyboardMarkups.DEFAULT_KEYBOARD
    )
)