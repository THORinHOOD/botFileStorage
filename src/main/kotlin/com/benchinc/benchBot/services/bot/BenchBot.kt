package com.benchinc.benchBot.services.bot

import com.benchinc.benchBot.data.AllSessions
import com.benchinc.benchBot.services.GeoService
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Location
import com.pengrad.telegrambot.model.Message
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
            callbackQuery.message()?.let { message ->
                callbackQuery.message().chat().id().let { chatId ->
                    when(callbackQuery.data()) {
                        "back" -> backPageCallback(message, callbackQuery.id())
                        "forward" -> forwardPageCallback(message, callbackQuery.id())
                        else -> telegramBot.execute(SendMessage(chatId, "Неизвестная команда"))
                    }
                }
            }
        }
    }

    private fun extractMessagePageNumber(message: String) : Int =
        message.substring(message.indexOf("(") + 1, message.indexOf("/")).toInt() - 1

    private fun forwardPageCallback(messagePage: Message, callbackId: String) {
        val page = extractMessagePageNumber(messagePage.text())
        val chatId = messagePage.chat().id()
        if ((page + 1) * 5 < sessions.getBenches(chatId).size) {
            sendPageWithBenches(chatId, page + 1, callbackId)
        } else {
            telegramBot.execute(AnswerCallbackQuery(callbackId))
        }
    }

    private fun backPageCallback(messagePage: Message, callbackId: String) {
        val page = extractMessagePageNumber(messagePage.text())
        if (page > 0) {
            sendPageWithBenches(messagePage.chat().id(), page - 1, callbackId)
        } else {
            telegramBot.execute(AnswerCallbackQuery(callbackId))
        }
    }

    private fun processLocation(update: Update) {
        update.message()?.location()?.let { location ->
            update.message().chat().id().let { chatId ->
                findBenches(chatId, location)
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
            val pagesCount = benches.size / 5 + (if (benches.size % 5 == 0) 0 else 1)
            var result = "(${page + 1}/${pagesCount}) Найдено <b>${benches.size}</b> лавочек в радиусе <b>${sessions.getRadius(chatId)}</b> " +
                    "метров\n\n"
            val benchesSubList = benches.subList(page * 5, min(page * 5 + 5, benches.size))
            for ((index, value) in benchesSubList.iterator().withIndex()) {
                val realIndex = index + 1 + page * 5
                result += "<b>${realIndex}.</b> $value\nПоказать на карте: /bench_${value.node.id}\n\n"
            }
            callbackId?.let {
                telegramBot.execute(AnswerCallbackQuery(it))
            }

            telegramBot.execute(
                SendMessage(chatId, result)
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(
                        InlineKeyboardMarkup(
                            InlineKeyboardButton("Назад").callbackData("back"),
                            InlineKeyboardButton("Дальше").callbackData("forward")
                        )
                    )
            )
        }
    }

}