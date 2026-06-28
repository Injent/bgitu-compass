package ru.bgitu.app.feature.teachers.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import pro.respawn.apiresult.map
import pro.respawn.apiresult.onError
import pro.respawn.apiresult.onSuccess
import pro.respawn.apiresult.orElse
import ru.bgitu.app.core.analytics.CrashAnalytics
import ru.bgitu.app.core.common.exception.NetworkException
import ru.bgitu.app.core.data.TeachersRepository
import ru.bgitu.app.core.data.util.NetworkMonitor
import ru.bgitu.app.core.model.SearchResultTeacher
import ru.bgitu.app.core.util.searchAndSort

class TeacherSearchViewModel(
    private val teachersRepository: TeachersRepository,
    networkMonitor: NetworkMonitor,
    searchPrimaryTeacher: Boolean
) : ViewModel() {
    val searchState = TextFieldState()
    @OptIn(FlowPreview::class)
    private val searchQueryFlow = snapshotFlow { searchState.text }
    val allTeachers = combine(
        networkMonitor.isOnline.onStart { emit(true) },
        teachersRepository.teacherSearchHistory
    ) { _, history ->
        teachersRepository.search("")
            .onError { e -> CrashAnalytics.e(e, "Failed to get all teachers") }
            .map {
                it.map { it.copy(appearedBefore = it.teacher in history) }
            }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState = combine(
        searchQueryFlow,
        networkMonitor.isOnline.onStart { emit(true) },
        allTeachers,
    ) { query, isOnline, allTeachersResult ->
        if (!isOnline) return@combine TeacherSearchUiState.HasNoInternet
        val filteredTeachers = allTeachersResult.orElse { e ->
            if (e is NetworkException) return@combine TeacherSearchUiState.HasNoInternet
            return@combine TeacherSearchUiState.Error(e = e)
        }.let {
            if (it.isEmpty()) return@combine TeacherSearchUiState.NotPublishedYet
            if (query.isBlank()) return@let it
            searchAndSort(query.toString(), it)
        }
        if (filteredTeachers.isEmpty()) return@combine TeacherSearchUiState.Empty

        TeacherSearchUiState.Success(results = filteredTeachers)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherSearchUiState.Loading
        )
}

sealed interface TeacherSearchUiState {
    data class Success(val results: List<SearchResultTeacher>) : TeacherSearchUiState
    data object Empty : TeacherSearchUiState
    data object Loading : TeacherSearchUiState
    data class Error(val e: Exception) : TeacherSearchUiState
    data object NotPublishedYet : TeacherSearchUiState
    data object HasNoInternet : TeacherSearchUiState
}