package tmg.flashback.news

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import tmg.flashback.news.apis.autosport.AutosportRssRetrofit
import tmg.flashback.news.apis.autosport.buildRetrofitAutosport
import tmg.flashback.news.apis.autosport.convert
import tmg.flashback.news.apis.crashnet.CrashNetRssRetrofit
import tmg.flashback.news.apis.crashnet.buildRetrofitCrashNet
import tmg.flashback.news.apis.crashnet.convert
import tmg.flashback.news.apis.motorsport.MotorsportRssRetrofit
import tmg.flashback.news.apis.motorsport.buildRetrofitMotorsport
import tmg.flashback.news.apis.motorsport.convert
import tmg.flashback.news.apis.pitpass.PitPassRssRetrofit
import tmg.flashback.news.apis.pitpass.buildRetrofitPitPass
import tmg.flashback.news.apis.pitpass.convert
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.enums.NewsSource
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.news.Article

private val autosportRss: AutosportRssRetrofit = buildRetrofitAutosport(BuildConfig.DEBUG)
private val crashNetRss: CrashNetRssRetrofit = buildRetrofitCrashNet(BuildConfig.DEBUG)
private val pitPassRss: PitPassRssRetrofit = buildRetrofitPitPass(BuildConfig.DEBUG)
private val motorsportRss: MotorsportRssRetrofit = buildRetrofitMotorsport(BuildConfig.DEBUG)

class News(
    private val prefsDB: PrefsDB
) : NewsDB {

    private suspend fun getAutosport(): Response<List<Article>> = safelyRun { autosportRss.getFeed().mChannel?.convert(prefsDB) ?: emptyList() }
    private suspend fun getCrashNet(): Response<List<Article>> = safelyRun { crashNetRss.getFeed().mChannel?.convert(prefsDB) ?: emptyList() }
    private suspend fun getPitPass(): Response<List<Article>> = safelyRun { pitPassRss.getFeed().mChannel?.convert(prefsDB) ?: emptyList() }
    private suspend fun getMotorsport(): Response<List<Article>> = safelyRun { motorsportRss.getFeed().mChannel?.convert(prefsDB) ?: emptyList() }

    private suspend fun safelyRun(runner: suspend () -> List<Article>): Response<List<Article>> {
        return try {
            val articles = runner()
            Response(articles)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {
                Log.i("Flashback", "Network error [${e.code()}] ${e.message()}")
                Response(null, e.code())
            } else {
                Response(null, -1)
            }
        }
    }

    @Suppress("RemoveExplicitTypeArguments")
    override fun getNews(): Flow<Response<List<Article>>> = flow {

        withContext(GlobalScope.coroutineContext) {

            val listOfRequests: MutableList<suspend () -> Response<List<Article>>> = mutableListOf<suspend () -> Response<List<Article>>>().apply {
                if (!prefsDB.newsSourceExcludeList.contains(NewsSource.AUTO_SPORT)) {
                    add(::getAutosport)
                }
                if (!prefsDB.newsSourceExcludeList.contains(NewsSource.CRASH_NET)) {
                    add(::getCrashNet)
                }
                if (!prefsDB.newsSourceExcludeList.contains(NewsSource.PIT_PASS)) {
                    add(::getPitPass)
                }
                if (!prefsDB.newsSourceExcludeList.contains(NewsSource.MOTORSPORT)) {
                    add(::getMotorsport)
                }
            }

            // TODO: Some weird quirk of varargs - Find a better way to do this shit
            @Suppress("RemoveExplicitTypeArguments")
            val responses: List<Response<List<Article>>> = when (listOfRequests.size) {
                1 -> listOf(listOfRequests[0].invoke())
                2 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke())
                3 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke())
                4 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke(), listOfRequests[3].invoke())
                5 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke(), listOfRequests[3].invoke(), listOfRequests[4].invoke())
                6 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke(), listOfRequests[3].invoke(), listOfRequests[4].invoke(), listOfRequests[5].invoke())
                7 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke(), listOfRequests[3].invoke(), listOfRequests[4].invoke(), listOfRequests[5].invoke(), listOfRequests[6].invoke())
                8 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke(), listOfRequests[3].invoke(), listOfRequests[4].invoke(), listOfRequests[5].invoke(), listOfRequests[6].invoke(), listOfRequests[7].invoke())
                9 -> listOf(listOfRequests[0].invoke(), listOfRequests[1].invoke(), listOfRequests[2].invoke(), listOfRequests[3].invoke(), listOfRequests[4].invoke(), listOfRequests[5].invoke(), listOfRequests[6].invoke(), listOfRequests[7].invoke(), listOfRequests[8].invoke())
                else -> emptyList<Response<List<Article>>>()
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
                        emit(Response<List<Article>>(null, -1))
                    } else {
                        emit(errors.first())
                    }
                }
                else {
                    emit(Response<List<Article>>(emptyList()))
                }
            }
        }
    }
}