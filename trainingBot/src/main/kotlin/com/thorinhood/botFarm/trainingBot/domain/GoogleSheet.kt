package com.thorinhood.botFarm.trainingBot.domain

typealias GoogleRow = List<String>

data class GoogleSheet(
    val range: String,
    val majorDimension: String,
    val values: List<GoogleRow>
)