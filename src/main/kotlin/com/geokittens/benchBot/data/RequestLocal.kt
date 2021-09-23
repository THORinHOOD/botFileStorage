package com.geokittens.benchBot.data

import com.db.benchLib.data.PointGeo

class RequestLocal(
    var chatId: Long,
    var location: PointGeo
)