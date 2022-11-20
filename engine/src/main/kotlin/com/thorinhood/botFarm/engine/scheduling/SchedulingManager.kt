package com.thorinhood.botFarm.engine.scheduling

import com.thorinhood.botFarm.configs.TelegramMessage
import com.thorinhood.botFarm.engine.messages.MsgSender
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.scheduling.config.ScheduledTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.config.TriggerTask
import org.springframework.stereotype.Service
import java.util.*

@Service
class SchedulingManager(
    private val scheduledTaskRegistrar: ScheduledTaskRegistrar,
    private val msgSender: MsgSender<TelegramMessage>
) : Logging {

    private val scheduledTasks = mutableMapOf<String, ScheduledTask>()

    fun addTask(
        taskId: String,
        sessionId: Long,
        task: (MsgSender<TelegramMessage>) -> Unit,
        getPeriod: (Long) -> Long
    ) {
        if (scheduledTasks.containsKey(taskId)) {
            throw IllegalArgumentException("Task is already scheduled!!! $taskId")
        }
        val triggerTask = TriggerTask({ task(msgSender) }) { triggerContext ->
            val period = getPeriod(sessionId) // TODO what if exception
            if (period <= 0L) {
                Date(2100, 1, 1)
            } else {
                val lastExecution = triggerContext.lastScheduledExecutionTime()
                if (lastExecution == null) {
                    Date(triggerContext.clock.millis() + period)
                } else {
                    Date(lastExecution.time + period)
                }
            }
        }
        scheduledTaskRegistrar.scheduleTriggerTask(triggerTask)?.let {
            scheduledTasks[taskId] = it
        }
        logger.info("Scheduled task $taskId")
    }

    fun removeTask(taskId: String) {
        if (scheduledTasks.containsKey(taskId)) {
            scheduledTasks[taskId]!!.cancel()
            scheduledTasks.remove(taskId)
            logger.info("Removed and canceled task : $taskId")
        } else {
            logger.info("Tried to remove task that was already removed: $taskId")
        }
    }

}