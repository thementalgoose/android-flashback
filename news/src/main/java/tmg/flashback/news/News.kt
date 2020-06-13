package tmg.flashback.news

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import tmg.flashback.news.apis.autosport.AutosportRssRetrofit
import tmg.flashback.news.apis.autosport.buildRetrofitAutosport
import tmg.flashback.news.apis.autosport.convert
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.models.Response
import tmg.flashback.repo.models.news.NewsItem

private val autosportRss: AutosportRssRetrofit = buildRetrofitAutosport(true)

class News: NewsDB {

    override fun syncAll() {

    }

    override fun getNews(): Flow<Response<List<NewsItem>>> = flow {
        try {
            val allResults = autosportRss
                .getFeed().mChannel?.convert() ?: emptyList<NewsItem>()
            this.emit(Response(allResults))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {
                Log.i("Flashback", "HTTP Status code ${e.code()}")
                Log.i("Flashback", "HTTP Status message ${e.message()}")
            }
            this.emit(Response<List<NewsItem>>(null, 500))
        }
    }
}