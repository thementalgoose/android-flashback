package tmg.flashback.news

import androidx.annotation.LayoutRes
import tmg.flashback.R
import tmg.flashback.home.list.HomeItem
import tmg.flashback.repo.models.news.Article

sealed class NewsItem(
    @LayoutRes val layoutId: Int
) {
    data class News(
        val item: Article
    ): NewsItem(R.layout.view_news_news)

    data class Message(
        val msg: String
    ): NewsItem(R.layout.view_shared_message)

    object NoNetwork: NewsItem(R.layout.view_shared_no_network)

    object InternalError: NewsItem(R.layout.view_shared_internal_error)

    object AllSourcesDisabled: NewsItem(R.layout.view_shared_no_news_sources)


}