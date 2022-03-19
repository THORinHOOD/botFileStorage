package com.thorinhood.fileStorageBot.data

data class FileTreeInfo(
    var currentPath: String,
    val indexToEntity: MutableMap<String, Entity>,
    var offset: Int,
    var limit: Int
)