package com.thorinhood.fileStorageBot.services.api

import com.thorinhood.fileStorageBot.data.EntitiesListResponse

interface FileStorageService {
    fun getAuthLink() : String
    fun getToken(code: String) : String
    fun getEntities(token: String, path: String, offset: Int, limit: Int) : EntitiesListResponse
}