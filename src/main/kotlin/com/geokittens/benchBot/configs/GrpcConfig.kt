package com.geokittens.benchBot.configs

import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.thorinhood.benchLib.proto.services.BenchServiceGrpc

@Configuration
class GrpcConfig(
    @Value("\${benchesService.host}") private val benchesServiceHost: String,
    @Value("\${benchesService.port}") private val benchesServicePort: Int
) {

    @Bean
    fun benchServiceStub() : BenchServiceGrpc.BenchServiceBlockingStub {
        val channel = ManagedChannelBuilder.forAddress(benchesServiceHost, benchesServicePort)
            .usePlaintext()
            .build()
        return BenchServiceGrpc.newBlockingStub(channel)
    }

}