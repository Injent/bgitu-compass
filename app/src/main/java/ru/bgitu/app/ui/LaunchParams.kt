package ru.bgitu.app.ui

import kotlinx.datetime.LocalDate

data class LaunchParams(
    val date: LocalDate? = null,
    val groupId: Int? = null
)
