package ru.bgitu.app

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.disk.DiskCache
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.initialize
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.bgitu.app.di.appModule

class CompassApp : Application(), SingletonImageLoader.Factory {

    val applicationScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CompassApp)
            modules(appModule)
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: PlatformContext): ImageLoader {
        val diskCache = DiskCache.Builder()
            .directory(cacheDir.resolve("image_cache").toOkioPath())
            .maxSizePercent(0.02)
            .build()

        return ImageLoader.Builder(this)
            .crossfade(true)
            .components {
                add(KtorNetworkFetcherFactory(get<HttpClient>()))
                if (SDK_INT >= 28) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache(diskCache)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
            .build()
    }
}