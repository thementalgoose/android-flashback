package tmg.flashback.rss.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import tmg.flashback.rss.network.BuildConfig
import tmg.flashback.rss.network.RssAPI
import tmg.flashback.rss.network.interceptor.CleanupXmlInterceptor
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class RSSModule {

    @Provides
    fun provideRssAPI(
        @RssRetrofit
        retrofit: Retrofit
    ): RssAPI {
        return retrofit.create(RssAPI::class.java)
    }

    @Provides
    @Singleton
    @RssRetrofit
    fun providesRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl("https://testing")
            .addConverterFactory(SimpleXmlConverterFactory.create())

        val client = OkHttpClient.Builder()
        client.addInterceptor(CleanupXmlInterceptor)
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(interceptor)
        }
        builder.client(client.build())

        return builder.build()
    }
}

@Qualifier
internal annotation class RssRetrofit