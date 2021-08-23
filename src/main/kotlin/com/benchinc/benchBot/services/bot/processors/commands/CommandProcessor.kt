package com.benchinc.benchBot.services.bot.processors.commands

import com.benchinc.benchBot.services.bot.processors.Processor

interface CommandProcessor : Processor<String> {
    val commandName : String
}