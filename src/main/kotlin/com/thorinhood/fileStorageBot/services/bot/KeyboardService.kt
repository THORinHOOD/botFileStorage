package com.thorinhood.fileStorageBot.services.bot

import com.pengrad.telegrambot.model.request.KeyboardButton
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.thorinhood.fileStorageBot.data.Session
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions.StartEntitiesProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.default_pipeline.transitions.StartAuthProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.MoveToMainProcessor
import com.thorinhood.fileStorageBot.services.bot.processors.file_tree_pipeline.PrevFolderProcessor
import org.springframework.stereotype.Service

@Service
class KeyboardService {

    fun getDefaultKeyboard(session: Session) : ReplyKeyboardMarkup =
        session.token?.let { token ->
            DEFAULT_KEYBOARD
        } ?: UNAUTH_KEYBOARD

    companion object {
        private val UNAUTH_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(StartAuthProcessor.NAME))
        ).resizeKeyboard(true)
        private val DEFAULT_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(StartEntitiesProcessor.NAME))
        ).resizeKeyboard(true)

        val FILE_TREE_KEYBOARD: ReplyKeyboardMarkup = ReplyKeyboardMarkup(
            arrayOf(KeyboardButton(PrevFolderProcessor.NAME)),
            arrayOf(KeyboardButton(MoveToMainProcessor.NAME))
        )
    }

}