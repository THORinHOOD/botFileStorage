package com.thorinhood.fileStorageBot.chatBotEngine.sessions

class Session(var chatId: Long,
              var token: String?,
              var cursor: Cursor,
              val fileTreeInfo: FileTreeInfo
)