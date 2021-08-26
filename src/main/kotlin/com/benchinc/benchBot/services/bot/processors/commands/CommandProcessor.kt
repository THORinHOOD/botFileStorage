package com.benchinc.benchBot.services.bot.processors.commands

import com.benchinc.benchBot.services.bot.processors.Processor
import com.pengrad.telegrambot.model.Message

interface CommandProcessor : Processor<String> {
    val commandName : String
}