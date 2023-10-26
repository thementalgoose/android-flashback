package tmg.flashback.rss.presentation.feed

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