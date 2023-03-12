package com.thorinhood.botFarm.configs

import com.thorinhood.botFarm.engine.scheduling.SchedulingManager
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

@Configuration
@EnableScheduling
class ScheduleConfig (
    @Value("\${engine.scheduler.corePoolSize:3}") private val schedulerCorePoolSize: Int
) : DisposableBean {

    private val scheduledExecutorService: ScheduledExecutorService =
        Executors.newScheduledThreadPool(schedulerCorePoolSize)

    @Bean
    @Lazy
    fun scheduledExecutorService() = scheduledExecutorService

    @Bean
    @Lazy
    fun scheduledTaskRegistrar(scheduledExecutorService: ScheduledExecutorService) : ScheduledTaskRegistrar {
        val registrar = ScheduledTaskRegistrar()
        registrar.setScheduler(scheduledExecutorService)
        return registrar
    }

    override fun destroy() {
        scheduledExecutorService.shutdown()
    }

}
