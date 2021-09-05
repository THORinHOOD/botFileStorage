package com.benchinc.benchBot.services.bot.helpers.strategies

import com.benchinc.benchBot.data.RequestLocal
import com.benchinc.benchBot.services.RequestsServiceLocal
import com.db.benchLib.clients.RequestsServiceClient
import com.db.benchLib.data.request.NewRequestDto

import com.db.benchLib.services.MultipartFileService
import org.springframework.stereotype.Service

@Service
class AddBenchStrategyImpl(
    private val multipartFileService: MultipartFileService,
    private val requestsServiceLocal: RequestsServiceLocal,
    private val requestsServiceClient: RequestsServiceClient
) : AddBenchStrategy {

    override fun commitRequest(chatId: Long, photo: ByteArray) : String {
        val photoMultipart = multipartFileService.buildFromPhoto(photo)
        val request = requestsServiceLocal.getRequest(chatId)
        return requestsServiceClient.addRequest(photoMultipart, NewRequestDto.builder()
            .location(request.location)
            .properties(mapOf())
            .build())
    }

}