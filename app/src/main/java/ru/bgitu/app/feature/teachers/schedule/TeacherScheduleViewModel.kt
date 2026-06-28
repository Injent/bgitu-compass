package ru.bgitu.app.feature.teachers.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.impl.Schedulers.schedule
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.yearMonth
import pro.respawn.apiresult.onError
import pro.respawn.apiresult.onSuccess
import pro.respawn.apiresult.orElse
import ru.bgitu.app.core.common.exception.NetworkException
import ru.bgitu.app.core.data.TeachersRepository
import ru.bgitu.app.core.data.util.AcademicWeekProvider
import ru.bgitu.app.core.data.util.NetworkMonitor
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.model.TeacherLesson
import ru.bgitu.app.core.util.isEvenAcademicWeek
import ru.bgitu.app.core.util.now
import ru.bgitu.app.feature.teachers.schedule.model.LessonsGroupedByDay
import ru.bgitu.app.feature.teachers.schedule.model.LessonsGroupedByMonth

class TeacherScheduleViewModel(
    private val teachersRepository: TeachersRepository,
    academicWeekProvider: AcademicWeekProvider,
    networkMonitor: NetworkMonitor,
    private val teacher: Teacher
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = networkMonitor.isOnline
        .onStart { emit(true) }
        .flatMapLatest { isOnline ->
            combine(
                teachersRepository.favoriteTeachers,
                academicWeekProvider.termStartDate
            ) { favoriteTeachers, termStartDate ->
                val isFavorite = teacher in favoriteTeachers

                val schedule = teachersRepository.getTeacherSchedule(teacher = teacher)
                    .orElse { e ->
                        if (e is NetworkException) return@combine TeacherScheduleUiState.HasNoInternet(
                            teacher = teacher,
                            isFavorite = isFavorite
                        )
                        return@combine TeacherScheduleUiState.Error(
                            teacher = teacher,
                            isFavorite = isFavorite
                        )
                    }

                if (!isOnline) {
                    return@combine TeacherScheduleUiState.Error(
                        teacher = teacher,
                        isFavorite = isFavorite
                    )
                }

                val currentDate = LocalDate.now() // TODO переделать на динамический источник
                val lessons = (currentDate..currentDate.plus(2, DateTimeUnit.WEEK)).flatMap { date ->
                    schedule.getDaySchedule(date, termStartDate).map { it.copy(lessonDate = date) }
                }

                if (lessons.isEmpty()) {
                    return@combine TeacherScheduleUiState.Empty(
                        teacher = teacher,
                        isFavorite = isFavorite
                    )
                }

                TeacherScheduleUiState.Success(
                    lessons = lessons.groupByMonth(),
                    teacher = teacher,
                    isFavorite = isFavorite
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherScheduleUiState.Loading(teacher = teacher)
        )

    fun updateFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                teachersRepository.addToFavorites(teacher = teacher)
            } else {
                teachersRepository.removeFromFavorites(teacher = teacher)
            }
        }
    }
}

sealed interface TeacherScheduleUiState {
    val teacher: Teacher
    val isFavorite: Boolean

    data class Success(
        val lessons: ImmutableList<LessonsGroupedByMonth>,
        override val teacher: Teacher,
        override val isFavorite: Boolean
    ) : TeacherScheduleUiState

    data class Empty(
        override val teacher: Teacher,
        override val isFavorite: Boolean
    ) : TeacherScheduleUiState

    data class Error(
        override val teacher: Teacher,
        override val isFavorite: Boolean
    ) : TeacherScheduleUiState

    data class Loading(
        override val teacher: Teacher,
    ) : TeacherScheduleUiState {
        override val isFavorite: Boolean = false
    }

    data class HasNoInternet(
        override val teacher: Teacher,
        override val isFavorite: Boolean,
    ) : TeacherScheduleUiState
}

private fun List<TeacherLesson>.groupByMonth(): ImmutableList<LessonsGroupedByMonth> {
    return this
        .filter { it.lessonDate != null }
        .groupBy { it.lessonDate!!.yearMonth }
        .map { (yearMonth, lessonsInMonth) ->
            val daysList = lessonsInMonth
                .groupBy { it.lessonDate }
                .map { (date, lessonsInDay) ->
                    LessonsGroupedByDay(
                        date = date!!,
                        lessons = lessonsInDay.sortedBy { it.startAt }.toImmutableList()
                    )
                }
                .sortedBy { it.date }

            LessonsGroupedByMonth(
                yearMonth = yearMonth,
                days = daysList.toImmutableList()
            )
        }
        .sortedBy { it.yearMonth }
        .toImmutableList()
}
