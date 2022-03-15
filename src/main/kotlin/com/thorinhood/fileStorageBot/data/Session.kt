package com.thorinhood.fileStorageBot.data

class Session(var chatId: Long,
              var token: String?,
              var currentPath: String,
              var currentPipelineInfo: PipelineInfo)