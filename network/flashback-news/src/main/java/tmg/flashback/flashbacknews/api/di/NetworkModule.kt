package tmg.flashback.flashbacknews.api.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import tmg.flashback.flashbacknews.api.NewsNetworkConfigManager
import tmg.flashback.flashbacknews.api.api.FlashbackNewsApi
import tmg.flashback.flashbacknews.api.usecases.GetNewsUseCase
import tmg.flashback.flashbacknews.api.usecases.GetNewsUseCaseImpl
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    @Provides
    fun provideGetNewsUseCase(impl: GetNewsUseCaseImpl): GetNewsUseCase = impl

    @Provides
    fun provideFlashbackNewsApi(
        @FlashbackNewsRetrofit
        retrofit: Retrofit
    ): FlashbackNewsApi {
        return retrofit.create(FlashbackNewsApi::class.java)
    }

    @Provides
    @Singleton
    @FlashbackNewsRetrofit
    fun providesRetrofit(
        @ApplicationContext
        context: Context,
        baseUrlManager: NewsNetworkConfigManager,
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

        builder.client(client.build())
        return builder.build()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class FlashbackNewsRetrofit