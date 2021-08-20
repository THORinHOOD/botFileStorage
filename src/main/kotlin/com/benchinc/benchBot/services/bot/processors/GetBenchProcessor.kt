package com.benchinc.benchBot.services.bot.processors

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.request.AbstractSendRequest
import com.pengrad.telegrambot.request.SendLocation
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.stereotype.Service

@Service
class GetBenchProcessor : CommandProcessor {

    override fun getCommandName(): String = "bench"

    override fun process(session: Session, text: String): AbstractSendRequest<*> {
        val benchIndex = text.substring(7).toInt() - 1
        return session.currentBenches.let { benches ->
            if (benchIndex >= benches.size || benchIndex < 0) {
                SendMessage(session.chatId, "Некорректный индекс лавочки")
            } else {
                SendLocation(
                    session.chatId,
                    benches[benchIndex].node.position.latitude.toFloat(),
                    benches[benchIndex].node.position.longitude.toFloat()
                )
            }
        }
    }

}