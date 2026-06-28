package ru.bgitu.app.core.model

sealed interface PresenceState {
    data class Success(val present: Int, val total: Int) : PresenceState

    data object Connecting : PresenceState

    data object Disconnected : PresenceState
}