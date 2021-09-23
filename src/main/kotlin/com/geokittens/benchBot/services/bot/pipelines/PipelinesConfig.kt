package com.geokittens.benchBot.services.bot.pipelines

import com.geokittens.benchBot.services.bot.processors.CancelPipelineProcessor
import com.geokittens.benchBot.services.bot.processors.Pipeline
import com.geokittens.benchBot.services.bot.processors.Processor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PipelinesConfig {

    @Bean
    fun defaultPipeline(@Pipeline("default") processors: List<Processor>) : PipelineProcessor {
        return PipelineProcessor("default", processors.associateBy { it.name })
    }

    @Bean
    fun changeRadiusPipeline(@Pipeline("change_radius") processors: MutableList<Processor>,
                             cancelPipelineProcessor: CancelPipelineProcessor) : PipelineProcessor {
        processors.add(cancelPipelineProcessor)
        return PipelineProcessor("change_radius", processors.associateBy { it.name })
    }

    @Bean
    fun addBenchPipeline(@Pipeline("add_bench") processors: MutableList<Processor>,
                         cancelPipelineProcessor: CancelPipelineProcessor) : PipelineProcessor {
        processors.add(cancelPipelineProcessor)
        return PipelineProcessor("add_bench", processors.associateBy { it.name })
    }

}