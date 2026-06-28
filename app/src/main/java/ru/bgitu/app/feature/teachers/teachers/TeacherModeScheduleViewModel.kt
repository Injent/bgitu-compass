package ru.bgitu.app.feature.teachers.teachers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import ru.bgitu.app.core.data.TeachersRepository
import ru.bgitu.app.core.data.schedule.ScheduleRepository
import ru.bgitu.app.core.data.schedule.TeacherScheduleSource
import ru.bgitu.app.core.data.util.AcademicWeekProvider
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.model.TeacherSchedule
import ru.bgitu.app.core.util.now

class TeacherModeScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val teachersRepository: TeachersRepository,
    academicWeekProvider: AcademicWeekProvider,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _selectedDate =
        savedStateHandle.getMutableStateFlow(KEY_START_DATE, LocalDate.now())
    val startDate = _selectedDate.asStateFlow()

    val uiState: StateFlow<TeacherScheduleUiState> = teachersRepository.primaryTeacher
        .flatMapLatest { teacher ->
            if (teacher == null) return@flatMapLatest flowOf(
                TeacherScheduleUiState(
                    scheduleState = TeacherScheduleState.TeacherNotSelected
                )
            )
            combine(
                scheduleRepository.getTeacherScheduleFlow(teacher),
                academicWeekProvider.termStartDate
            ) { source, termStartDate ->
                val scheduleState = when (source) {
                    is TeacherScheduleSource.Dropped -> TeacherScheduleState.TeacherNotSelected
                    is TeacherScheduleSource.Failed -> TeacherScheduleState.Failed(source.throwable)
                    is TeacherScheduleSource.Loading -> TeacherScheduleState.Loading
                    is TeacherScheduleSource.Success -> TeacherScheduleState.Success(source.schedule)
                }

                TeacherScheduleUiState(
                    teacher = teacher,
                    scheduleState = scheduleState,
                    termStartDate = termStartDate
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherScheduleUiState()
        )

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setTeacher(teacher: Teacher) {
        viewModelScope.launch {
            teachersRepository.setPrimaryTeacher(teacher)
        }
    }
}

data class TeacherScheduleUiState(
    val teacher: Teacher? = null,
    val scheduleState: TeacherScheduleState = TeacherScheduleState.Loading,
    val termStartDate: LocalDate = LocalDate.now()
)

sealed interface TeacherScheduleState {
    data class Success(val schedule: TeacherSchedule) : TeacherScheduleState
    data object Loading : TeacherScheduleState
    data class Failed(val throwable: Throwable) : TeacherScheduleState
    data object TeacherNotSelected : TeacherScheduleState
}

private const val KEY_START_DATE = "teacherStartDate"