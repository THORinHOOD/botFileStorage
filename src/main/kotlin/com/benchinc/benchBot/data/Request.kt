package com.benchinc.benchBot.data

import com.db.benchLib.data.PointGeo

class Request(
    var chatId: Long,
    var location: PointGeo
)