package tmg.flashback.flashbackapi.api.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import tmg.flashback.flashbackapi.api.NetworkConfigManager
import tmg.flashback.flashbackapi.api.api.FlashbackApi
import tmg.flashback.flashbackapi.api.interceptor.ConfigUrlInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideFlashbackApi(
        @FlashbackRetrofit
        retrofit: Retrofit
    ): FlashbackApi {
        return retrofit.create(FlashbackApi::class.java)
    }

    @Provides
    @Singleton
    @FlashbackRetrofit
    fun providesRetrofit(
        baseUrlManager: NetworkConfigManager,
        configUrlInterceptor: ConfigUrlInterceptor
    ): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }

        val builder = Retrofit.Builder()
            .baseUrl(baseUrlManager.defaultBaseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))

        val client = OkHttpClient.Builder()
        client.callTimeout(10L, TimeUnit.SECONDS)

        // Debug
        if (baseUrlManager.isDebug) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(interceptor)
        }

        // Config URL Intercept
        client.addInterceptor(configUrlInterceptor)

        builder.client(client.build())
        return builder.build()
    }
}

@Qualifier
internal annotation class FlashbackRetrofit