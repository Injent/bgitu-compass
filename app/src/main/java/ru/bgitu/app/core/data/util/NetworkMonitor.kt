package ru.bgitu.app.core.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkCapabilities.TRANSPORT_VPN
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class NetworkMonitor(
    private val context: Context,
    applicationScope: CoroutineScope,
) {
    private data class NetworkStateSnapshot(
        val hasInternet: Boolean,
        val hasValidatedInternet: Boolean,
        val hasWifi: Boolean,
        val hasVpn: Boolean
    )

    @OptIn(FlowPreview::class)
    val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(
                NetworkStateSnapshot(
                    hasInternet = false,
                    hasValidatedInternet = false,
                    hasWifi = false,
                    hasVpn = false
                )
            )
            channel.close()
            return@callbackFlow
        }

        val activeCapabilities = mutableMapOf<Network, NetworkCapabilities>()

        fun emitCurrentState() {
            val capabilitiesList = activeCapabilities.values
            val snapshot = NetworkStateSnapshot(
                hasInternet = capabilitiesList.any {
                    it.hasCapability(NET_CAPABILITY_INTERNET) || it.hasTransport(TRANSPORT_VPN)
                },
                hasValidatedInternet = capabilitiesList.any { it.hasCapability(NET_CAPABILITY_VALIDATED) },
                hasWifi = capabilitiesList.any { it.hasTransport(TRANSPORT_WIFI) },
                hasVpn = capabilitiesList.any { it.hasTransport(TRANSPORT_VPN) }
            )
            channel.trySend(snapshot)
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                activeCapabilities[network] = networkCapabilities
                emitCurrentState()
            }

            override fun onLost(network: Network) {
                activeCapabilities.remove(network)
                emitCurrentState()
            }
        }

        val request = NetworkRequest.Builder()
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        .mapLatest { snapshot ->
            snapshot.hasInternet
        }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .conflate()
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1
        )

    private suspend fun pingApi(): Boolean = withContext(Dispatchers.IO) {
        try {
            val connection = URL("https://api-ssl.bgitu-compass.ru/").openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            (connection.responseCode == 200).also {
                Log.d("NetworkMonitor", "Connection established")
            }
        } catch (_: Exception) {
            Log.d("NetworkMonitor", "Failed to establish connection")
            false
        }
    }
}