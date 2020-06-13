package tmg.flashback.news.apis.autosport

import org.simpleframework.xml.*

const val autosportDateFormat: String = "EEE, dd MMM yyyy HH:mm:ss Z"

@Root(name = "rss", strict = false)
data class AutosportRssModel @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val mChannel: AutosportRssChannelModel? = null
)

@Root(name = "channel", strict = false)
data class AutosportRssChannelModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "description")
    @param:Element(name = "description")
    val mDescription: String? = null,
    @field:Element(name = "language")
    @param:Element(name = "language")
    val mLanguage: String? = null,
    @field:Element(name = "copyright")
    @param:Element(name = "copyright")
    val mCopyright: String? = null,
    @field:Element(name = "ttl")
    @param:Element(name = "ttl")
    val mTtl: Int? = null,
    @field:Element(name = "lastBuildDate")
    @param:Element(name = "lastBuildDate")
    val mLastBuildDate: String? = null,
    @field:Element(name = "image")
    @param:Element(name = "image")
    val mImage: AutosportRssImageModel? = null,
    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    val mItem: List<AutosportRssItemModel>? = null
)

@Root(name = "item", strict = false)
data class AutosportRssItemModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "description")
    @param:Element(name = "description")
    val mDescription: String? = null,
    @field:Element(name = "link")
    @param:Element(name = "link")
    val mLink: String? = null,
    @field:Element(name = "guid")
    @param:Element(name = "guid")
    val mGuid: String? = null,
    @field:Element(name = "category")
    @param:Element(name = "category")
    val mCategory: String? = null,
    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    val mPubDate: String? = null
)

@Root(name = "image", strict = false)
data class AutosportRssImageModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "url")
    @param:Element(name = "url")
    val mUrl: String? = null
)