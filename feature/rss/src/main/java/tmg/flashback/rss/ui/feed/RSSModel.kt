package tmg.flashback.rss.ui.feed

import androidx.annotation.LayoutRes
import tmg.flashback.rss.R
import tmg.flashback.rss.repo.model.Article

sealed class RSSModel(
    val key: String
) {
    data class RSS(
        val item: Article
    ): RSSModel(
        key = item.id
    )

    data class Message(
        val msg: String,
        val id: String = msg,
    ): RSSModel(
        key = id
    )

    object Advert: RSSModel(
        key = "advert"
    )

    object NoNetwork: RSSModel(
        key = "advert"
    )

    object InternalError: RSSModel(
        key = "advert"
    )

    object SourcesDisabled: RSSModel(
        key = "advert"
    )
}