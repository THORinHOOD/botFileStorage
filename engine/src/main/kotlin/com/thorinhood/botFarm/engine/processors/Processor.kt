package com.thorinhood.botFarm.engine.processors

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
@Service
annotation class Processor