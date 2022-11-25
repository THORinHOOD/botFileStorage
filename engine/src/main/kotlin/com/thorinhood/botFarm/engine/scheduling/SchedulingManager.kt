package com.thorinhood.botFarm.engine.scheduling

import com.thorinhood.botFarm.configs.TelegramMessage
import com.thorinhood.botFarm.engine.messages.MsgSender
import com.thorinhood.botFarm.engine.sessions.Session
import com.thorinhood.botFarm.engine.sessions.SessionsService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.scheduling.config.ScheduledTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.config.TriggerTask
import org.springframework.stereotype.Service
import java.util.*

@Service
class SchedulingManager(
    private val scheduledTaskRegistrar: ScheduledTaskRegistrar,
    private val sessionsService: SessionsService,
    private val msgSender: MsgSender<TelegramMessage>
) : Logging {

    private val scheduledTasks = mutableMapOf<String, ScheduledTask>()

    fun addTask(
        scheduleConfig: ScheduleConfig,
        sessionId: Long,
        arguments: Map<String, Any>,
        task: (Session<Long>, Map<String, Any>) -> List<TelegramMessage>,
        getPeriod: (Session<Long>) -> Long
    ) {
        if (scheduledTasks.containsKey(scheduleConfig.taskId)) {
            throw IllegalArgumentException("Task is already scheduled!!! ${scheduleConfig.taskId}")
        }
        val triggerTask = TriggerTask({
            val innerSession = sessionsService.getSession(sessionId)
            val messages = task(innerSession, arguments)
            sessionsService.updateSession(innerSession)
            msgSender.sendMessages(messages)
        }) { triggerContext ->
            val lastExecution = triggerContext.lastScheduledExecutionTime()
            val period = if (lastExecution == null) {
                scheduleConfig.period
            } else {
                val innerSession = sessionsService.getSession(sessionId)
                getPeriod(innerSession) // TODO what if exception
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