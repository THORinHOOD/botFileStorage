package com.benchinc.benchBot.data

class Session(var chatId: Long,
              var currentBenches: List<Bench>,
              var radius: Int,
              var nextProcessor: String?)