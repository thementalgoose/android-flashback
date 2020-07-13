package tmg.flashback.news

import androidx.annotation.LayoutRes
import tmg.flashback.R
import tmg.flashback.repo.models.news.Article
import tmg.flashback.shared.SyncDataItem

sealed class NewsItem(
    @LayoutRes val layoutId: Int
) {
    data class News(
        val item: Article
    ): NewsItem(R.layout.view_news_news)

    data class Message(
        val msg: String
    ): NewsItem(R.layout.view_shared_message)

    data class ErrorItem(
        val item: SyncDataItem
    ): NewsItem(item.layoutId)
}

fun MutableList<NewsItem>.addError(syncDataItem: SyncDataItem) {
    this.add(NewsItem.ErrorItem(syncDataItem))
}