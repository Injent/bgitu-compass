package ru.bgitu.app.core.analytics

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.crashlytics

object CrashAnalytics {

    private val crashlytics: FirebaseCrashlytics
        get() = Firebase.crashlytics
    private const val TAG = "CrashAnalytics"

    fun e(throwable: Throwable, message: String? = null) {
        if (message != null) {
            logBreadcrumb("Error Context: $message")
        }
        crashlytics.recordException(throwable)
        Log.e(TAG, "Non-fatal error: $message", throwable)
    }

    fun logBreadcrumb(message: String) {
        Log.d(TAG, message)
        crashlytics.log(message)
    }

    fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }

    fun setCurrentScreen(screenName: String) {
        logBreadcrumb("Navigated to: $screenName")
        setCustomKey("current_screen", screenName)
    }
}