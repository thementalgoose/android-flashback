package tmg.flashback.rss

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.RSSDB
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.rss.Article
import tmg.flashback.rss.apis.convert
import tmg.flashback.rss.shared.RssXMLRetrofit
import tmg.flashback.rss.shared.buildRetrofit
import java.lang.NullPointerException
import java.lang.RuntimeException
import java.net.UnknownHostException
import javax.xml.stream.XMLStreamException


class RSS(
    private val prefsDB: PrefsDB
) : RSSDB {
    override fun getNews(): Flow<Response<List<Article>>> = flow {

        withContext(GlobalScope.coroutineContext) {

            val xmlRetrofit: RssXMLRetrofit = buildRetrofit(true)

            val responses: MutableList<Response<List<Article>>> = mutableListOf()
            for (x in prefsDB.rssUrls) {
                try {
                    val response = xmlRetrofit.getRssXML(x).convert(x, prefsDB.rssShowDescription)
                    responses.add(Response(response))
                } catch (e: XMLStreamException) {
                    responses.add(Response(null, -1))
                } catch (e: RuntimeException) {
                    responses.add(Response(null, -1))
                } catch (e: UnknownHostException) {
                    responses.add(Response(null, -1))
                } catch (e: HttpException) {
                    responses.add(Response(null, e.code()))
                } catch (e: NullPointerException) {
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