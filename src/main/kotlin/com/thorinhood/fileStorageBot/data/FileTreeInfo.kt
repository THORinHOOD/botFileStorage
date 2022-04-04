package com.thorinhood.fileStorageBot.data

import com.thorinhood.fileStorageBot.data.Entity

data class FileTreeInfo(
    var currentPath: String,
    val indexToEntity: MutableMap<String, Entity>,
    var offset: Int,
    var limit: Int
)