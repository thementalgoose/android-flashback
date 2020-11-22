package tmg.flashback.rss.shared

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import tmg.flashback.rss.BuildConfig

inline fun <reified T> buildRetrofit(isXML: Boolean = true): T {
    var builder = Retrofit.Builder()
        .baseUrl("https://jordanfisher.io")

    builder = if (isXML) {
        builder.addConverterFactory(SimpleXmlConverterFactory.create())
    }
    else {
        builder.addConverterFactory(GsonConverterFactory.create())
    }
    val client = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)
    }

    builder.client(client.build())

    val retrofit: Retrofit = builder.build()
    return retrofit.create(T::class.java)
}