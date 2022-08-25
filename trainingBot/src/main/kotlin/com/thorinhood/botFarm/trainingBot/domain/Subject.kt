package com.thorinhood.botFarm.trainingBot.domain

typealias AllSubjects = MutableMap<String, Subject>

class Subject(
    var name: String,
    var googleTableId: String,
    var googleTableSheet: String,
    var timerConfig: TimerConfig,
    var lessonSize: Int = 10
) {
    constructor(builder: Builder, interval: Long) : this(
        builder.name,
        builder.googleTableId,
        builder.googleTableSheet,
        TimerConfig.makeNew(interval)
    )

    class Builder {
        var name: String = ""
            private set
        var googleTableId: String = ""
            private set
        var googleTableSheet: String = ""
            private set
        fun name(name: String) = apply { this.name = name }
        fun googleTableId(googleTableId: String) = apply { this.googleTableId = googleTableId }
        fun googleTableSheet(googleTableSheet: String) = apply { this.googleTableSheet = googleTableSheet }
    }
}