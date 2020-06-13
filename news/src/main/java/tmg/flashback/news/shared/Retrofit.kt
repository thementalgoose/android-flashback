package tmg.flashback.news.shared

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


inline fun <reified T> buildRetrofit(baseUrl: String, isXML: Boolean = true, isDebug: Boolean = false): T {
    var builder = Retrofit.Builder()
        .baseUrl(baseUrl)

    builder = if (isXML) {
        builder.addConverterFactory(SimpleXmlConverterFactory.create())
    }
    else {
        builder.addConverterFactory(GsonConverterFactory.create())
    }
    if (isDebug) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        builder.client(client)
    }

    val retrofit: Retrofit = builder.build()
    return retrofit.create(T::class.java)
}