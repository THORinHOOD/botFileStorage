package com.benchinc.benchBot.services.bot.pipelines

import com.benchinc.benchBot.data.Session
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.BaseRequest
import org.springframework.stereotype.Service

@Service
class PipelinesManagerImpl(pipelines: List<PipelineProcessor>) : PipelinesManager {

    private val mapPipelines : Map<String, PipelineProcessor> = pipelines.associateBy { it.name }

    override fun process(session: Session, update: Update): List<BaseRequest<*, *>> =
        mapPipelines[session.currentPipelineInfo.pipelineName]?.process(session, update) ?:
        throw IllegalArgumentException("Pipeline ${session.currentPipelineInfo.pipelineName} not found")

}