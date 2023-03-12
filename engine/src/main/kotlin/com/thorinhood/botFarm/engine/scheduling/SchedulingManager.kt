package com.thorinhood.botFarm.engine.scheduling

import com.thorinhood.botFarm.engine.messages.BotSender
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.scheduling.config.ScheduledTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.config.TriggerTask
import java.util.*

class SchedulingManager<IS, OS>(
    private val scheduledTaskRegistrar: ScheduledTaskRegistrar,
    private val botSender: BotSender<OS, IS>?
) : Logging {

    private val scheduledTasks = mutableMapOf<String, ScheduledTask>()

    fun addTask(
        scheduleConfig: ScheduleConfig,
        sessionId: Long,
        arguments: Map<String, Any>,
        task: (Long, Map<String, Any>) -> List<IS>,
        getPeriod: (Long) -> Long
    ) {
        if (scheduledTasks.containsKey(scheduleConfig.taskId)) {
            throw IllegalArgumentException("Task is already scheduled!!! ${scheduleConfig.taskId}")
        }
        val triggerTask = TriggerTask({
            val messages = task(sessionId, arguments)
            botSender?.sendMessages(messages)
        }) { triggerContext ->
            val lastExecution = triggerContext.lastScheduledExecutionTime()
            val period = if (lastExecution == null) {
                scheduleConfig.period
            } else {
                getPeriod(sessionId) // TODO what if exception
            }
            if (period <= 0L) {
                Date(2100, 1, 1)
            } else {
                if (lastExecution == null) {
                    Date(triggerContext.clock.millis() + period)
                } else {
                    Date(lastExecution.time + period)
                }
            }
        }
        scheduledTaskRegistrar.scheduleTriggerTask(triggerTask)?.let {
            scheduledTasks[scheduleConfig.taskId] = it
        }
        logger.info("Scheduled task ${scheduleConfig.taskId}")
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