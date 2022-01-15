package tmg.flashback.rss.network

import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import tmg.flashback.rss.BuildConfig
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.network.apis.convert
import tmg.flashback.rss.network.shared.RssXMLRetrofit
import tmg.flashback.rss.network.shared.buildRetrofit
import tmg.flashback.rss.repo.RSSRepository
import tmg.flashback.rss.repo.RssAPI
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import javax.xml.stream.XMLStreamException

class RSS(
    private val repository: RSSRepository,
    private val rssFeedController: RSSController
) : RssAPI {

    private val xmlRetrofit: RssXMLRetrofit = buildRetrofit(true)
    private val headers: Map<String, String> = mapOf(
        "Accept" to "application/rss+xml, application/xml"
    )

    private fun get(url: String): Flow<Response<List<Article>>> = flow {
        val result: Response<List<Article>> = try {
            val response = xmlRetrofit.getRssXML(headers, url)
                .convert(rssFeedController, url, repository.rssShowDescription)
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

        emit(result)
    }

    private fun getAll(): Flow<List<Response<List<Article>>>> = flow {
        if (repository.rssUrls.isEmpty()) { emit(emptyList()) }
        combine(repository.rssUrls.map { get(it) }) {
            it.toList()
        }.collect {
            emit(it)
        }
    }

    override fun getNews(): Flow<Response<List<Article>>> = getAll()
        .map { responses ->
            val errors = responses.filter { it.code != 200 }
            if (responses.size != errors.size) {
                // Only some requests failed, continue with list
                val validResponses = responses
                    .filter { it.result != null }
                    .mapNotNull { it.result }
                    .flatten()
                    .sortedByDescending { it.date }
                return@map Response(validResponses)
            } else {
                // All failed
                if (errors.isNotEmpty()) {
                    if (errors.any { it.isNoNetwork }) {
                        @Suppress("RemoveExplicitTypeArguments")
                        return@map Response<List<Article>>(null, -1)
                    } else {
                        return@map errors.first()
                    }
                } else {
                    @Suppress("RemoveExplicitTypeArguments")
                    return@map Response<List<Article>>(emptyList())
                }
            }
        }
    }