package ru.bgitu.app.core.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import pro.respawn.apiresult.ApiResult
import ru.bgitu.app.core.data.schedule.asExternalModel
import ru.bgitu.app.core.datastore.model.UserPrefs
import ru.bgitu.app.core.model.SearchResultTeacher
import ru.bgitu.app.core.model.Teacher
import ru.bgitu.app.core.model.TeacherLesson
import ru.bgitu.app.core.network.model.NetworkTeacherLesson
import ru.bgitu.app.core.network.service.CompassApi
import ru.bgitu.app.core.util.now

class TeachersRepository(
    private val compassApi: CompassApi,
    private val dataStore: DataStore<UserPrefs>
) {
    val primaryTeacher: Flow<Teacher?> = dataStore.data.map {
        it.teacherModeTeacher?.let(::Teacher)
    }
        .distinctUntilChanged()
    val showTeacherSuggestion = dataStore.data.map {
        it.showTeacherSwitchSuggestion
    }
    val userIsTeacher: Flow<Boolean> = dataStore.data.map {
        it.userIsTeacher
    }
    val teacherSearchHistory = dataStore.data.map {
        it.teacherSearchHistory.map { name -> Teacher(name) }
    }
    val favoriteTeachers = dataStore.data.map {
        it.favoriteTeachers.map { name -> Teacher(name) }
    }

    suspend fun dismissSwitchToTeacherSuggestion() {
        dataStore.updateData {
            it.copy(showTeacherSwitchSuggestion = false)
        }
    }

    suspend fun setPrimaryTeacher(teacher: Teacher?) {
        dataStore.updateData { it.copy(teacherModeTeacher = teacher?.fullName) }
    }

    suspend fun setUserIsTeacher(isTeacher: Boolean) {
        dataStore.updateData {
            it.copy(userIsTeacher = isTeacher)
        }
    }

    suspend fun addToFavorites(teacher: Teacher) {
        dataStore.updateData { prefs ->
            prefs.copy(
                favoriteTeachers = listOf(teacher.fullName) +
                        prefs.favoriteTeachers.filter { it != teacher.fullName }
            )
        }
    }

    suspend fun removeFromFavorites(teacher: Teacher) {
        dataStore.updateData { prefs ->
            prefs.copy(
                favoriteTeachers = prefs.favoriteTeachers - teacher.fullName
            )
        }
    }

    suspend fun search(query: String) = ApiResult {
        val history = dataStore.data.first().teacherSearchHistory
        compassApi.searchTeachers(query)
            .distinctBy { it.name }
            .map {
                SearchResultTeacher(
                    appearedBefore = it.name in history,
                    teacher = Teacher(it.name)
                )
            }
    }

    suspend fun getTeacherSchedule(teacher: Teacher) = ApiResult {
        dataStore.updateData { prefs ->
            prefs.copy(
                teacherSearchHistory = listOf(teacher.fullName) +
                        prefs.teacherSearchHistory.filter { it != teacher.fullName }
                            .let {
                                if (it.size > 30) it.dropLast(1)
                                else it
                            }
            )
        }
        compassApi.getTeacherSchedule(teacher).asExternalModel()
    }

    suspend fun deleteTeacherFromHistory(teacher: Teacher) {
        dataStore.updateData {
            it.copy(teacherSearchHistory = it.teacherSearchHistory - teacher.fullName)
        }
    }
}

private fun NetworkTeacherLesson.toExternalModel() = TeacherLesson(
    subjectName = subjectName,
    building = building,
    classroom = classroom,
    startAt = startAt,
    endAt = endAt,
    isLecture = isLecture,
    groups = groups,
    lessonDate = lessonDate ?: LocalDate.now()
)