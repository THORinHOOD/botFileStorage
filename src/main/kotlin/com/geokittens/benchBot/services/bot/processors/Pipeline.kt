package com.geokittens.benchBot.services.bot.processors

import org.springframework.beans.factory.annotation.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class Pipeline(val pipelineName : String)
