package tmg.flashback.news

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.threeten.bp.LocalDateTime
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
import tmg.flashback.news.apis.skysports_unofficial.SkySportsRssRetrofit
import tmg.flashback.news.apis.skysports_unofficial.buildRetrofitSkySports
import tmg.flashback.news.apis.skysports_unofficial.convert
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.enums.NewsSource
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.news.Article

private val autosportRss: AutosportRssRetrofit = buildRetrofitAutosport(BuildConfig.DEBUG)
private val crashNetRss: CrashNetRssRetrofit = buildRetrofitCrashNet(BuildConfig.DEBUG)
private val pitPassRss: PitPassRssRetrofit = buildRetrofitPitPass(BuildConfig.DEBUG)
private val skySports: SkySportsRssRetrofit = buildRetrofitSkySports(BuildConfig.DEBUG)

class News(
    private val prefsDB: PrefsDB,
    private val isLive: Boolean
) : NewsDB {

    suspend fun allFeeds(): List<Article> {
        val newsList: MutableList<Article> = mutableListOf()
        if (!prefsDB.newsSourceExcludeList.contains(NewsSource.AUTO_SPORT)) {
            newsList += (autosportRss.getFeed().mChannel?.convert() ?: emptyList())
        }
        if (!prefsDB.newsSourceExcludeList.contains(NewsSource.CRASH_NET)) {
            newsList += (crashNetRss.getFeed().mChannel?.convert() ?: emptyList())
        }
        if (!prefsDB.newsSourceExcludeList.contains(NewsSource.PIT_PASS)) {
            newsList += (pitPassRss.getFeed().mChannel?.convert() ?: emptyList())
        }
        val sortedList = newsList.sortedByDescending { it.date ?: LocalDateTime.now() }
        if (!isLive && !prefsDB.newsSourceExcludeList.contains(NewsSource.PIT_PASS)) {
            val skySports = skySports.getFeed().convert()
            return sortedList + skySports
        }
        return sortedList
    }

    override fun syncAll() {

    }

    override fun getNews(): Flow<Response<List<Article>>> = flow {
        try {
            val allResults = allFeeds()

            this.emit(Response(allResults))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {
                Log.i("Flashback", "HTTP Status code ${e.code()}")
                Log.i("Flashback", "HTTP Status message ${e.message()}")
                this.emit(Response<List<Article>>(null, e.code()))
            }
            else {
                this.emit(Response<List<Article>>(null, -1))
            }
        }
    }
}