package tmg.flashback.rss.network

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import tmg.flashback.rss.BuildConfig
import tmg.flashback.rss.network.apis.convert
import tmg.flashback.rss.repo.RssRepository
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.Response
import tmg.flashback.rss.usecases.GetSupportedSourceUseCase
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException
import javax.xml.stream.XMLStreamException

class RssService @Inject constructor(
    private val rssAPI: RssAPI,
    private val repository: RssRepository,
    private val getSupportedSourceUseCase: GetSupportedSourceUseCase
) {

    private val headers: Map<String, String> = mapOf(
        "Accept" to "application/rss+xml, application/xml"
    )

    private suspend fun get(url: String): Response<List<Article>> {
        return try {
            val response = rssAPI
                .getRssXML(headers, url)
                .convert(getSupportedSourceUseCase, url, repository.rssShowDescription)
            Response(response)
        } catch (e: XMLStreamException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        } catch (e: SocketTimeoutException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        } catch (e: SSLHandshakeException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        } catch (e: ConnectException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        } catch (e: RuntimeException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        } catch (e: UnknownHostException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        } catch (e: HttpException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, e.code())
        } catch (e: NullPointerException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            Response(null, -1)
        }
    }

    private suspend fun getAll(): List<Response<List<Article>>> = coroutineScope {
        if (repository.rssUrls.isEmpty()) {
            return@coroutineScope emptyList()
        }
        return@coroutineScope repository.rssUrls
            .map { async { get(it) } }
            .awaitAll()
    }

    suspend fun getNews(): Response<List<Article>> {
        val responses = getAll()
        val errors = responses.filter { it.code != 200 }
        if (responses.size != errors.size) {
            // Only some requests failed, continue with list
            val validResponses = responses
                .filter { it.result != null }
                .mapNotNull { it.result }
                .flatten()
                .sortedByDescending { it.date }
            return Response(validResponses)
        } else {
            // All failed
            return if (errors.isNotEmpty()) {
                if (errors.any { it.isNoNetwork }) {
                    Response(null, -1)
                } else {
                    errors.first()
                }
            } else {
                Response(emptyList())
            }
        }

    }
}