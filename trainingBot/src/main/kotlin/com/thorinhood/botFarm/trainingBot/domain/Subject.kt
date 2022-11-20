package com.thorinhood.botFarm.trainingBot.domain

typealias AllSubjects = MutableMap<String, Subject>

class Subject(
    var name: String,
    var googleTableId: String,
    var googleTableSheet: String,
    var scheduleConfig: ScheduleConfig,
    var lessonSize: Int = 10
) {

    class Builder {
        var name: String = ""
            private set
        var googleTableId: String = ""
            private set
        var googleTableSheet: String = ""
            private set

        var scheduleConfig: ScheduleConfig = ScheduleConfig("", -1)
            private set

        fun name(name: String) = apply { this.name = name }
        fun googleTableId(googleTableId: String) = apply { this.googleTableId = googleTableId }
        fun googleTableSheet(googleTableSheet: String) = apply { this.googleTableSheet = googleTableSheet }

        fun scheduleConfig(scheduleConfig: ScheduleConfig) = apply { this.scheduleConfig = scheduleConfig }

        fun build() = Subject(name, googleTableId, googleTableSheet, scheduleConfig)
    }
}