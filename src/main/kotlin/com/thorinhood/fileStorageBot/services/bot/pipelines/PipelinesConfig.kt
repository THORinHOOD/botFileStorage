package com.thorinhood.fileStorageBot.services.bot.pipelines

import com.thorinhood.fileStorageBot.services.bot.processors.Pipeline
import com.thorinhood.fileStorageBot.services.bot.processors.Processor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PipelinesConfig {

    @Bean
    fun defaultPipeline(@Pipeline("default") processors: List<Processor>) : PipelineProcessor {
        return PipelineProcessor("default", processors.associateBy { it.name })
    }

    @Bean
    fun authPipeline(@Pipeline("auth") processors: List<Processor>) : PipelineProcessor {
        return PipelineProcessor("auth", processors.associateBy { it.name })
    }

    @Bean
    fun fileTreePipeline(@Pipeline("file_tree") processors: List<Processor>) : PipelineProcessor {
        return PipelineProcessor("file_tree", processors.associateBy { it.name })
    }

}