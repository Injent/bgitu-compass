package ru.bgitu.app.feature.teachers

import android.os.Parcelable
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.utilui.LocalFeatures
import ru.bgitu.app.feature.teachers.schedule.TeacherScheduleScreen
import ru.bgitu.app.feature.teachers.search.TeacherSearchScreen
import ru.bgitu.app.feature.teachers.teachers.TeacherModeScheduleScreen
import ru.bgitu.app.feature.teachers.teachers.TeachersScreen
import ru.bgitu.app.feature.teachers.teachers.TeachersViewModel
import ru.bgitu.app.navigation.Navigator
import ru.bgitu.app.ui.EntryProvider

@Parcelize
@Serializable
data object TeachersKey : NavKey, Parcelable

@Parcelize
@Serializable
data class TeachersSearchKey(val resultKey: String? = null) : NavKey, Parcelable

@Parcelize
@Serializable
data class TeacherScheduleKey(val teacherName: String) : NavKey, Parcelable {
    val teacher: Teacher
        get() = Teacher(teacherName)
}

fun EntryProvider.teachersGraph(
    navigator: Navigator
) {
    entry<TeachersKey>(
        clazzContentKey = { it },
    ) {
        val userIsTeacher = LocalFeatures.current.userIsTeacher

        if (userIsTeacher) {
            TeacherModeScheduleScreen(
                onNavigateToTeacherSearch = { resultKey ->
                    navigator.navigate(TeachersSearchKey(resultKey = resultKey))
                }
            )
        } else {
            TeachersScreen(
                viewModel = koinViewModel<TeachersViewModel>(),
                onTeacherClick = {
                    navigator.navigate(TeacherScheduleKey(it.fullName))
                },
                onNavigateToTeacherSearch = {
                    navigator.navigate(TeachersSearchKey())
                },
                onBack = navigator::goBack
            )
        }
    }
    entry<TeachersSearchKey>(
        clazzContentKey = { it },
        metadata = NavDisplay.transitionSpec {
            val from = this.initialState.key

            val enterTransition = when (from) {
                is TeachersKey -> slideInVertically { it }
                else -> EnterTransition.None
            }

            val exitTransition = when (from) {
                is TeachersKey -> fadeOut()
                else -> ExitTransition.None
            }

            enterTransition togetherWith exitTransition
        } + NavDisplay.popTransitionSpec {
            val to = this.targetState.key

            val enterTransition = when (to) {
                is TeachersKey -> fadeIn()
                else -> EnterTransition.None
            }
            val exitTransition = when (to) {
                is TeachersKey -> slideOutVertically { it }
                else -> ExitTransition.None
            }

            enterTransition togetherWith exitTransition
        }
    ) { key ->
        TeacherSearchScreen(
            onBack = navigator::goBack,
            onNavigateToTeacherSchedule = { teacher ->
                navigator.replaceLast(TeacherScheduleKey(teacherName = teacher.fullName))
            },
            resultKey = key.resultKey
        )
    }
    entry<TeacherScheduleKey>(
        clazzContentKey = { it },
        metadata = NavDisplay.transitionSpec {
            val from = this.initialState.key

            val enterTransition = when (from) {
                is TeachersSearchKey -> slideInHorizontally(DefaultSpringSpec) { it }
                is TeachersKey -> slideInVertically { it }
                else -> EnterTransition.None
            }
            val exitTransition = when (from) {
                is TeachersSearchKey -> slideOutHorizontally(DefaultSpringSpec) { -it }
                is TeachersKey -> slideOutVertically { it / 2 }
                else -> ExitTransition.None
            }

            enterTransition togetherWith exitTransition
        } + NavDisplay.popTransitionSpec {
            val to = this.targetState.key

            val enterTransition = when (to) {
                is TeachersKey -> slideInVertically { it / 2 }
                else -> EnterTransition.None
            }
            val exitTransition = when (to) {
                is TeachersKey -> slideOutVertically { -it / 2 } + fadeOut()
                else -> ExitTransition.None
            }
            enterTransition togetherWith exitTransition
        }
    ) { key ->
        TeacherScheduleScreen(
            teacher = key.teacher,
            onBack = navigator::goBack
        )
    }
}

const val SHARED_TRANSITION_KEY_SEARCHBAR = "teacherSearchBar"
private val DefaultSpringSpec = spring<IntOffset>(Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)