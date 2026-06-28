package ru.bgitu.app.core.model

import ru.bgitu.app.core.util.Searchable

data class Group(
    val id: Int,
    val name: String
) : Searchable {
    val degree: Degree
        get() = when {
            name.contains("спо", ignoreCase = true) -> Degree.SPE
            name.substringBefore('-').last() == 'м' -> Degree.MASTER
            else -> Degree.BACHELOR
        }

    enum class Degree {
        BACHELOR,
        MASTER,
        SPE,
    }

    override fun text(): String = name
}
