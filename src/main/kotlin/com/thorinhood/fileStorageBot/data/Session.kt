package com.thorinhood.fileStorageBot.data

class Session(var chatId: Long,
              var token: String?,
              var currentPipelineInfo: PipelineInfo,
              val fileTreeInfo: FileTreeInfo,
              var argument: String?)