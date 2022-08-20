package com.thorinhood.botFarm.trainingBot.domain

import java.util.Queue

data class Lesson(
    val tasks: Queue<Task>
)