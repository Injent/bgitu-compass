package ru.bgitu.app.core.util

interface Searchable {
    fun text(): String
}

fun <T : Searchable> searchAndSort(query: String, groups: List<T>): List<T> {
    val cleanQuery = normalize(query)
    if (cleanQuery.isEmpty()) return emptyList()

    val queryTokens = cleanQuery.split(" ").filter { it.isNotEmpty() }

    return groups
        .map { original ->
            val normalizedItem = normalize(original.text())
            val score = calculateScore(normalizedItem, queryTokens)
            original to score
        }
        .filter { it.second > 0 }
        .sortedByDescending { it.second }
        .map { it.first }
}

private fun normalize(text: String): String {
    return text.lowercase()
        .replace(Regex("[\\-()№]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun calculateScore(item: String, queryTokens: List<String>): Int {
    var totalScore = 0
    val itemTokens = item.split(" ")

    for (qToken in queryTokens) {
        totalScore += when {
            itemTokens.contains(qToken) -> 100
            item.contains(qToken) -> 10
            else -> return 0
        }
    }

    if (item.startsWith(queryTokens.first())) {
        totalScore += 50
    }

    return totalScore
}