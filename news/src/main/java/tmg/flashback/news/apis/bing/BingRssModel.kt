package tmg.flashback.news.apis.bing

import org.simpleframework.xml.*

const val bingDateFormat: String = "EEE, dd MMM yyyy HH:mm:ss Z"

val bingSourceColours: Map<String, String> = mapOf(
    "thecheckeredflag" to "#FF6604",
    "the daily telegraph" to "#06BBA0",
    "bbc" to "#FFCB2B",
    "motorsport" to "#000000",
    "autosport" to "#ff0000"
)

@Root(name = "rss", strict = false)
data class BingRssModel @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val mChannel: BingRssChannelModel? = null
)

@Root(name = "channel", strict = false)
data class BingRssChannelModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "description")
    @param:Element(name = "description")
    val mDescription: String? = null,
    @field:Element(name = "copyright")
    @param:Element(name = "copyright")
    val mCopyright: String? = null,
    @field:Element(name = "lastBuildDate")
    @param:Element(name = "lastBuildDate")
    val mLastBuildDate: String? = null,
    @field:Element(name = "image")
    @param:Element(name = "image")
    val mImage: BingRssImageModel? = null,
    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    val mItem: List<BingRssItemModel>? = null
)

@Root(name = "item", strict = false)
data class BingRssItemModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "description")
    @param:Element(name = "description")
    val mDescription: String? = null,
    @field:Element(name = "link")
    @param:Element(name = "link")
    val mLink: String? = null,
    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    val mPubDate: String? = null,

    @field:Element(name = "News:Source")
    @param:Element(name = "News:Source")
    val mSource: String? = null,
    @field:Element(name = "News:Image")
    @param:Element(name = "News:Image")
    val mImage: String? = null
)

@Root(name = "image", strict = false)
data class BingRssImageModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "url")
    @param:Element(name = "url")
    val mUrl: String? = null
)