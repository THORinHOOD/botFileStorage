package com.benchinc.benchBot.configs.clients

import com.benchinc.benchBot.data.Bench
import feign.Param
import feign.QueryMap
import feign.RequestLine
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

interface BenchesServiceClient {

    @RequestLine("GET /{id}")
    fun findBenchById(@PathVariable("id") id : String) : Bench

    @RequestLine("GET /find?lat={lat}&lon={lon}&radius={radius}")
    fun findBenchesNear(@Param("lon") lon: Float, @Param("lat") lat: Float,
                        @Param("radius") radius: Float) : List<Bench>

}