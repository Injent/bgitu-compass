package ru.bgitu.app.feature.teachers.teachers

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.app.core.data.TeachersRepository
import ru.bgitu.app.core.model.SearchResultTeacher
import ru.bgitu.app.core.model.Teacher

class TeachersViewModel(
    private val teachersRepository: TeachersRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(
        teachersRepository.teacherSearchHistory,
        teachersRepository.favoriteTeachers,
        teachersRepository.showTeacherSuggestion
    ) { history, favoriteTeachers, showTeacherSuggestion ->
        TeachersUiState.Success(
            favoriteTeachers = favoriteTeachers.toImmutableList(),
            showTeacherSuggestion = showTeacherSuggestion,
            history = history
                .map { SearchResultTeacher(appearedBefore = true, teacher = it) }
                .toImmutableList()
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeachersUiState.Loading
        )

    fun onFavoriteClick(teacher: Teacher) {
        viewModelScope.launch {
            teachersRepository.removeFromFavorites(teacher)
        }
    }

    fun dismissSwitchToTeacherDialog() {
        viewModelScope.launch {
            teachersRepository.dismissSwitchToTeacherSuggestion()
        }
    }

    fun switchToTeacher() {
        viewModelScope.launch {
            teachersRepository.setUserIsTeacher(isTeacher = true)
            teachersRepository.dismissSwitchToTeacherSuggestion()
        }
    }

    fun onDeleteTeacher(teacher: Teacher) {
        viewModelScope.launch {
            teachersRepository.deleteTeacherFromHistory(teacher)
        }
    }
}


@Immutable
sealed interface TeachersUiState {

    data class Success(
        val favoriteTeachers: ImmutableList<Teacher>,
        val history: ImmutableList<SearchResultTeacher>,
        val showTeacherSuggestion: Boolean,
    ) : TeachersUiState

    data object Loading : TeachersUiState
}