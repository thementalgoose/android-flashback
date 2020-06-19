package tmg.flashback.news.apis.crashnet

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

const val crashNetDateFormat: String = "EEE, dd MMM yyyy HH:mm:ss Z"

@Root(name = "rss", strict = false)
data class CrashNetRssModel @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val mChannel: CrashNetRssChannelModel? = null
)

@Root(name = "channel", strict = false)
data class CrashNetRssChannelModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    val mDescription: String? = null,
    @field:Element(name = "language")
    @param:Element(name = "language")
    val mLanguage: String? = null,
    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    val mItem: List<CrashNetRssItemModel>? = null
)

@Root(name = "item", strict = false)
data class CrashNetRssItemModel @JvmOverloads constructor(
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
    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    val mPubDate: String? = null
)