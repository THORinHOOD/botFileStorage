package com.thorinhood.botFarm.trainingBot.spaces.settings.interval

import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.processors.baseProcessors.BaseCancelProcessor
import com.thorinhood.botFarm.engine.processors.data.Transition
import com.thorinhood.botFarm.trainingBot.statics.KeyboardMarkups
import com.thorinhood.botFarm.trainingBot.statics.ProcSpace

@Processor
class CancelChangeIntervalProcessor : BaseCancelProcessor(
    "cancel_change_interval",
    ProcSpace.CHANGE_INTERVAL,
    Transition(
        ProcSpace.DEFAULT,
       "Окей, не будем",
       KeyboardMarkups.DEFAULT_KEYBOARD
    )
)