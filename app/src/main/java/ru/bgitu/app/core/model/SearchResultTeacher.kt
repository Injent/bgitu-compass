package ru.bgitu.app.core.model

import ru.bgitu.app.core.util.Searchable

data class SearchResultTeacher(
    val appearedBefore: Boolean,
    val teacher: Teacher,
) : Searchable {
    override fun text(): String = teacher.fullName
}