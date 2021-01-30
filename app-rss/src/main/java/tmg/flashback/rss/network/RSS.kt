package tmg.flashback.rss.network

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.Headers
import retrofit2.HttpException
import tmg.flashback.data.models.Response
import tmg.flashback.rss.BuildConfig
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.network.apis.convert
import tmg.flashback.rss.network.shared.RssXMLRetrofit
import tmg.flashback.rss.network.shared.buildRetrofit
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.model.Article
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.xml.stream.XMLStreamException

class RSS(
    private val prefsRepository: RSSPrefsRepository,
    private val rssController: RSSController
) : RSSRepository {

    private val headers: Map<String, String> = mapOf(
            "Accept" to "application/rss+xml, application/xml"
    )

    override fun getNews(): Flow<Response<List<Article>>> = flow {

        withContext(GlobalScope.coroutineContext) {

            val xmlRetrofit: RssXMLRetrofit = buildRetrofit(true)

            val responses: MutableList<Response<List<Article>>> = mutableListOf()
            for (x in prefsRepository.rssUrls) {
                try {
                    val response = xmlRetrofit.getRssXML(headers, x).convert(rssController, x, prefsRepository.rssShowDescription)
                    responses.add(Response(response))
                } catch (e: XMLStreamException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                } catch (e: SocketTimeoutException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                } catch (e: SSLHandshakeException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                } catch (e: ConnectException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                } catch (e: RuntimeException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                } catch (e: UnknownHostException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                } catch (e: HttpException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, e.code()))
                } catch (e: NullPointerException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                    responses.add(Response(null, -1))
                }
            }

            val errors = responses.filter { it.code != 200 }
            if (responses.size != errors.size) {
                // Only some requests failed, continue with list
                val validResponses = responses
                    .filter { it.result != null }
                    .mapNotNull { it.result }
                    .flatten()
                    .sortedByDescending { it.date }
                emit(Response(validResponses))
            }
            else {
                // All failed
                if (errors.isNotEmpty()) {
                    if (errors.any { it.isNoNetwork }) {
                        @Suppress("RemoveExplicitTypeArguments")
                        emit(Response<List<Article>>(null, -1))
                    } else {
                        emit(errors.first())
                    }
                }
                else {
                    @Suppress("RemoveExplicitTypeArguments")
                    emit(Response<List<Article>>(emptyList()))
                }
            }
        }
    }
}