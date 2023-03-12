package com.thorinhood.botFarm.engine.processors.matchers

import com.thorinhood.botFarm.engine.messages.HasText
import java.util.function.Predicate

abstract class Matchers {
    companion object {
        fun <IR: HasText> eq(text: String, ignoreCase: Boolean = false) =
            Predicate<IR> { it.getText().equals(text, ignoreCase) }
        fun <IR: HasText> contains(text: String, ignoreCase: Boolean = false) =
            Predicate<IR> { it.getText().contains(text, ignoreCase) }
        fun <IR> always() : Predicate<IR> = Predicate<IR> { true }
        fun <IR> never() : Predicate<IR> = Predicate<IR> { false }
    }
}
