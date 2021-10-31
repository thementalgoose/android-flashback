package tmg.flashback.statistics.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import tmg.flashback.statistics.network.NetworkConfigManager
import tmg.flashback.statistics.network.api.FlashbackApi

val networkModule = module {

    factory { buildFlashbackApi(get()) }
    single { buildRetrofit(get()) }
}

private fun buildFlashbackApi(retrofit: Retrofit): FlashbackApi {
    return retrofit.create(FlashbackApi::class.java)
}

private fun buildRetrofit(baseUrlManager: NetworkConfigManager): Retrofit {
    val json = Json {
        ignoreUnknownKeys = true
    }

    val builder = Retrofit.Builder()
        .baseUrl(baseUrlManager.baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))

    val client = OkHttpClient.Builder()
    if (baseUrlManager.isDebug) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)
    }
    builder.client(client.build())

    return builder.build()
}