package com.thorinhood.botFarm.engine

import com.thorinhood.botFarm.engine.data.entities.TransitionsHistoryConfigured
import com.thorinhood.botFarm.engine.data.services.TransactionsHistoryDataService
import com.thorinhood.botFarm.engine.messages.BotReceiver
import com.thorinhood.botFarm.engine.messages.BotSender
import com.thorinhood.botFarm.engine.messages.HasSessionId
import com.thorinhood.botFarm.engine.processors.Processor
import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import org.apache.logging.log4j.kotlin.Logging

class Bot<OR, IR, IS, OS> : Logging where IR : HasSessionId {
    private val spaces = mutableMapOf<String, Space<IR, IS>>()
    private var receiver: BotReceiver<OR, IR>? = null
    private var sender: BotSender<OS, IS>? = null
    private var notFoundAnyProcessor: ((IR) -> IS)? = null
    private var schedulingManager: SchedulingManager<IS, OS>? = null
    private var smthWentWrongMessageBuilder: ((IR) -> IS)? = null

    fun space(name: String, block: Space<IR, IS>.() -> Unit) {
        if (spaces.containsKey(name)) {
            throw IllegalArgumentException("Space with name $name already exists")
        }
        val space = Space(name, notFoundAnyProcessor)
        space.block()
        spaces[name] = space
    }

    fun receiver(receiver: BotReceiver<OR, IR>) {
        this.receiver = receiver
    }

    fun sender(sender: BotSender<OS, IS>) {
        this.sender = sender
    }

    fun schedulingManager(schedulingManager: SchedulingManager<IS, OS>) {
        this.schedulingManager = schedulingManager
    }

    fun smthWentWrongMessageBuilder(smthWentWrongMessageBuilder: (IR) -> IS) {
        this.smthWentWrongMessageBuilder = smthWentWrongMessageBuilder
    }

    fun notFoundAnyProcessor(notFoundAnyProcessor: (IR) -> IS) {
        this.notFoundAnyProcessor = notFoundAnyProcessor
    }

    fun start(transitionsHistoryDataService: TransactionsHistoryDataService): Bot<OR, IR, IS, OS> {
        if (sender == null) {
            throw IllegalStateException("Can not start bot without sender!")
        }
        if (receiver == null) {
            throw IllegalStateException("Can not start bot without sender!")
        }
        receiver!!.subscribe { innerMessage ->
            try {
                transitionsHistoryDataService.workWith(innerMessage.getSessionId()) { transitionsHistory ->
                    val procSpace = transitionsHistory.getCurrentProcSpace()
                    val messages: List<IS> = if (!spaces.containsKey(procSpace)) {
                        logger.error("Did not find procSpace: $procSpace")
                        listOf()
                    } else {
                        spaces[procSpace]!!.process(innerMessage, transitionsHistory)
                    }
                    sender!!.sendMessages(messages)
                }
            } catch (ex: Exception) {
                logger.error(ex)
                smthWentWrongMessageBuilder?.let {
                    sender!!.sendMessage(it.invoke(innerMessage))
                }
            }
        }
        receiver!!.start()
        return this
    }

}

class Space<IR, IS>(
    val name: String,
    private val notFoundAnyProcessor: ((IR) -> IS)? = null
) : Logging where IR : HasSessionId {
    private val processors: MutableList<Processor<IR, IS>> = mutableListOf()
    private var defaultProcessor: Processor<IR, IS>? = null

    fun process(innerMessage: IR, transitionsHistory: TransitionsHistoryConfigured): List<IS> {
        val processors = processors.filter { processor ->
            processor.matcher?.test(innerMessage) ?: false
        }
        return if (processors.isEmpty() && defaultProcessor == null) {
            logger.error("Did not find processor for message in procSpace: $name")
            notFoundAnyProcessor?.let { listOf(it.invoke(innerMessage)) } ?: listOf()
        } else if (processors.isEmpty()) {
            executeProcessor(defaultProcessor!!, innerMessage, transitionsHistory)
        } else if (processors.size > 1) {
            logger.error("Found more than 1 processor in procSpace: $name, processors: $processors")
            listOf()
        } else {
            executeProcessor(processors.first(), innerMessage, transitionsHistory)
        }
    }

    fun processor(processor: Processor<IR, IS>, block: (Processor<IR, IS>.() -> Unit)? = null) {
        processor.procSpace = name
        block?.let { processor.it() }
        processors.add(processor)
    }

    fun defaultProcessor(defaultProcessor: Processor<IR, IS>, block: (Processor<IR, IS>.() -> Unit)? = null) {
        defaultProcessor.procSpace = name
        block?.let { defaultProcessor.it() }
        this.defaultProcessor = defaultProcessor
    }

    private fun executeProcessor(
        processor: Processor<IR, IS>,
        innerMessage: IR,
        transitionsHistory: TransitionsHistoryConfigured
    ): List<IS> {
        val messages = processor.process(innerMessage, transitionsHistory)
        processor.defaultTransition?.let {
            transitionsHistory.makeTransition(it)
        }
        return messages
    }
}

fun <OR, IR, IS, OS> bot(
    block: Bot<OR, IR, IS, OS>.() -> Unit
): Bot<OR, IR, IS, OS> where IR : HasSessionId {
    val bot = Bot<OR, IR, IS, OS>()
    bot.block()
    return bot
}