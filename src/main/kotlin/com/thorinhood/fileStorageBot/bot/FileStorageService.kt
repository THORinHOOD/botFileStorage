package com.thorinhood.fileStorageBot.bot

import com.thorinhood.fileStorageBot.chatBotEngine.pagination.ElementsResponse
import com.thorinhood.fileStorageBot.chatBotEngine.pagination.Entity

interface FileStorageService<T : Entity> {
    fun getAuthLink() : String
    fun getToken(code: String) : String
    fun getEntities(token: String, path: String, offset: Int, limit: Int) : ElementsResponse<T>
    fun createFolder(token: String, path: String) : Boolean
    fun deleteEntity(token: String, path: String, permanently: Boolean): Boolean
}