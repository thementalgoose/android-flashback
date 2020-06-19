package tmg.flashback.news.shared

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


inline fun <reified T> buildRetrofit(baseUrl: String, isXML: Boolean = true, isDebug: Boolean = false): T {
    var builder = Retrofit.Builder()
        .baseUrl(baseUrl)

    builder = if (isXML) {
        builder.addConverterFactory(SimpleXmlConverterFactory.create())
    }
    else {
        builder.addConverterFactory(GsonConverterFactory.create())
    }
    val client = OkHttpClient.Builder()
    if (isDebug) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)
    }

    builder.client(client.build())

    val retrofit: Retrofit = builder.build()
    return retrofit.create(T::class.java)
}