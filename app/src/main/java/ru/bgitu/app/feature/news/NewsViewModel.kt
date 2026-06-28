package ru.bgitu.app.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.bgitu.app.core.data.notifications.FirebaseNotificationsRepository
import ru.bgitu.app.core.model.News

class NewsViewModel(
    private val notificationsRepository: FirebaseNotificationsRepository
) : ViewModel() {

    private val _showOnlyUnreadNews = MutableStateFlow(false)

    val uiState = combine(
        _showOnlyUnreadNews,
        notificationsRepository.cachedNews
    ) { showOnlyUnreadNews, news ->
        if (news.isEmpty()) return@combine NewsUiState.Empty
        NewsUiState.Success(
            news = if (showOnlyUnreadNews) news.filter { !it.read } else news,
            showOnlyUnreadNews = showOnlyUnreadNews
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NewsUiState.Loading
        )

    fun toggleUnreadNotifications() {
        _showOnlyUnreadNews.value = !_showOnlyUnreadNews.value
    }

    fun markAsRead(notificationId: Int) {
        viewModelScope.launch {
            notificationsRepository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            notificationsRepository.markAllAsRead()
        }
    }
}

sealed interface NewsUiState {
    data class Success(
        val news: List<News>,
        val showOnlyUnreadNews: Boolean
    ) : NewsUiState {
        val hasUnreadNews: Boolean
            get() = news.any { !it.read }
    }

    data object Empty : NewsUiState

    data object Loading : NewsUiState
}