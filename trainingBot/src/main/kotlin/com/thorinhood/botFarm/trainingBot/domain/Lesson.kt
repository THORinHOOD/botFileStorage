package com.thorinhood.botFarm.trainingBot.domain

data class Lesson(
    var tasks: MutableList<Task>
) {
    fun hasTask(): Boolean = tasks.isNotEmpty()
    fun getCurrentTask() : Task = tasks.first()
    fun removeCurrentTask() {
        tasks.removeAt(0)
    }
}