package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.services.GeoService
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.*
import com.pengrad.telegrambot.request.*
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import kotlin.math.min

@Service
class BenchBot(val telegramBot: TelegramBot,
               val geoService: GeoService,
               val commandsProcessorsService: CommandsProcessorsService
) : UpdatesListener, Logging {

    val sessions : AllSessions = AllSessions()

    init {
        telegramBot.setUpdatesListener(this)
    }

    fun <T : AbstractSendRequest<T>> T.addReply(messageId: Int?) : T {
        messageId?.let {
            this.replyToMessageId(it)
        }
        return this
    }

    override fun process(updates: MutableList<Update>?): Int {
        updates?.forEach { update ->
            try {
                processCommands(update)
                processCallbacks(update)
                processLocation(update)
            } catch (e:Exception) {
                logger.error(e)
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    private fun processCommands(update: Update) {
        update.message()?.text()?.let { text ->
            update.message().chat().id().let { chatId ->
                telegramBot.execute(
                    commandsProcessorsService.process(sessions.getSession(chatId), update.message().messageId(),
                        text)
                )
            }
        }
    }

    private fun processCallbacks(update: Update) {
        update.callbackQuery()?.let { callbackQuery ->
            callbackQuery.message().chat().id().let { chatId ->
                callbackQuery.message().messageId().let { messageId ->
                    when(callbackQuery.data()) {
                        "back" -> backPageCallback(chatId, messageId, callbackQuery.id())
                        "forward" -> forwardPageCallback(chatId, messageId, callbackQuery.id())
                        else -> telegramBot.execute(SendMessage(chatId, "Неизвестная команда"))
                    }
                }
            }
        }
    }

    private fun forwardPageCallback(chatId: Long, messagePageId: Int, callbackId: String) {
        sessions.getMessagePage(chatId, messagePageId)?.let { page ->
            if ((page + 1) * 5 < sessions.getBenches(chatId).size) {
                sendPageWithBenches(chatId, page + 1, callbackId)
            } else {
                telegramBot.execute(AnswerCallbackQuery(callbackId))
            }
        }
    }

    private fun processLocation(update: Update) {
        update.message()?.location()?.let { location ->
            update.message().chat().id().let { chatId ->
                findBenches(chatId, location)
            }
        }
    }

    private fun backPageCallback(chatId: Long, messageId: Int, callbackId: String) {
        sessions.getMessagePage(chatId, messageId)?.let { page ->
            if (page > 0) {
                sendPageWithBenches(chatId, page - 1, callbackId)
            } else {
                telegramBot.execute(AnswerCallbackQuery(callbackId))
            }
        }
    }

    private fun findBenches(chatId: Long, location: Location) {
        sessions.setBenches(chatId, geoService.findBenches(location, sessions.getRadius(chatId)))
        if (sessions.getBenches(chatId).isEmpty()) {
            telegramBot.execute(
                SendMessage(chatId, "В радиусе ${sessions.getRadius(chatId)} метров " +
                        "лавочек не найдено \uD83D\uDE1E")
            )
        } else {
            sendPageWithBenches(chatId, 0, null)
        }
    }

    private fun sendPageWithBenches(chatId: Long, page: Int, callbackId: String?) {
        sessions.getBenches(chatId).let { benches ->
            var result = "Найдено <b>${benches.size}</b> лавочек в радиусе <b>${sessions.getRadius(chatId)}</b> " +
                    "метров\n\n"
            val benchesSubList = benches.subList(page * 5, min(page * 5 + 5, benches.size))
            for ((index, value) in benchesSubList.iterator().withIndex()) {
                val realIndex = index + 1 + page * 5
                result += "<b>${realIndex}.</b> $value\nПоказать на карте: /bench_$realIndex\n\n"
            }
            callbackId?.let {
                telegramBot.execute(AnswerCallbackQuery(it))
            }

            val response = telegramBot.execute(
                SendMessage(chatId, result)
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(
                        InlineKeyboardMarkup(
                            InlineKeyboardButton("Назад").callbackData("back"),
                            InlineKeyboardButton("Дальше").callbackData("forward")
                        )
                    )
            )
            sessions.addMessagePage(chatId, response.message().messageId(), page)
        }
    }

}