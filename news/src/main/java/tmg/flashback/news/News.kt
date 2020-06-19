package tmg.flashback.news

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import tmg.flashback.news.apis.autosport.AutosportRssRetrofit
import tmg.flashback.news.apis.autosport.buildRetrofitAutosport
import tmg.flashback.news.apis.autosport.convert
import tmg.flashback.news.apis.crashnet.CrashNetRssRetrofit
import tmg.flashback.news.apis.crashnet.buildRetrofitCrashNet
import tmg.flashback.news.apis.crashnet.convert
import tmg.flashback.news.apis.pitpass.PitPassRssRetrofit
import tmg.flashback.news.apis.pitpass.buildRetrofitPitPass
import tmg.flashback.news.apis.pitpass.convert
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.news.NewsItem

private val autosportRss: AutosportRssRetrofit = buildRetrofitAutosport(BuildConfig.DEBUG)
private val crashNetRss: CrashNetRssRetrofit = buildRetrofitCrashNet(BuildConfig.DEBUG)
private val pitPassRss: PitPassRssRetrofit = buildRetrofitPitPass(BuildConfig.DEBUG)

class News: NewsDB {

    suspend fun allFeeds(): List<NewsItem> {
        val newsList: MutableList<NewsItem> = mutableListOf()
        newsList += (autosportRss.getFeed().mChannel?.convert() ?: emptyList())
        newsList += (crashNetRss.getFeed().mChannel?.convert() ?: emptyList())
        newsList += (pitPassRss.getFeed().mChannel?.convert() ?: emptyList())
        return newsList.sortedByDescending { it.date }
    }

    override fun syncAll() {

    }

    override fun getNews(): Flow<Response<List<NewsItem>>> = flow {
        try {
            val allResults = allFeeds()

            delay(1000)

            this.emit(Response(allResults))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {
                Log.i("Flashback", "HTTP Status code ${e.code()}")
                Log.i("Flashback", "HTTP Status message ${e.message()}")
                this.emit(Response<List<NewsItem>>(null, e.code()))
            }
            else {
                this.emit(Response<List<NewsItem>>(null, -1))
            }
        }
    }
}