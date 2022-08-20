package com.thorinhood.botFarm.trainingBot.domain

data class Task(
    val question: String,
    val answers: List<String>
)