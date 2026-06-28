package ru.bgitu.app.core.model

@JvmInline
value class Teacher(val fullName: String) {
    val shortName: String
        get() = runCatching {
            val (lastName, firstName, middleName) = fullName.split(' ')
            "$lastName ${firstName[0]}.${middleName[0]}."
        }.getOrDefault(fullName)
}