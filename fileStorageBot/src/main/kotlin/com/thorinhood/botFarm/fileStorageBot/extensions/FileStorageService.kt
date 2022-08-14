package com.thorinhood.botFarm.fileStorageBot.extensions

import com.thorinhood.botFarm.engine.pagination.ElementsResponse

interface FileStorageService<T> {
    fun getAuthLink() : String
    fun getToken(code: String) : String
    fun getEntities(token: String, path: String, offset: Int, limit: Int) : ElementsResponse<T>
    fun createFolder(token: String, path: String) : Boolean
    fun deleteEntity(token: String, path: String, permanently: Boolean): Boolean
}