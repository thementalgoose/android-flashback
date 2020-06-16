package tmg.flashback.news.apis.bing

import android.net.Uri
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.repo.models.news.NewsItem
import tmg.flashback.repo.models.news.NewsSource
import java.util.*

//fun BingRssChannelModel.convert(): List<NewsItem> {
//    return this.mItem
//        ?.filter { it.mTitle != null && it.mLink != null && it.mPubDate != null && it.mSource != null }
//        ?.map {
//            NewsItem(
//                id = UUID.randomUUID().toString(),
//                title = it.mTitle!!,
//                description = it.mDescription ?: "",
//                link = it.mLink!!.extractLinkFromBingUrl(),
//                date = LocalDateTime.parse(it.mPubDate, DateTimeFormatter.ofPattern(bingDateFormat)),
//                source = NewsSource(
//                    source = it.mSource!!,
//                    image = it.mImage,
//                    link = it.mSource.extractHostFromBingUrl(),
//
//                ),
//            )
//        }
//}
//
//private fun String.extractHostFromBingUrl(): String {
//    return "google.com"
//}
//
//private fun String.extractLinkFromBingUrl(): String {
//    val uri = Uri.parse(this)
//
//    return "https://www.google.com"
//}