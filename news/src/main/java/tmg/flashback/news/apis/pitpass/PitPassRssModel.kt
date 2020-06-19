package tmg.flashback.news.apis.pitpass

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

const val pitPassDateFormat: String = "EEE, dd MMM yyyy HH:mm:ss Z"

@Root(name = "rss", strict = false)
data class PitPassRssModel @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val mChannel: PitPassRssChannelModel? = null
)

@Root(name = "channel", strict = false)
data class PitPassRssChannelModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:Element(name = "description", required = false)
    @param:Element(name = "description", required = false)
    val mDescription: String? = null,
    @field:Element(name = "language")
    @param:Element(name = "language")
    val mLanguage: String? = null,
    @field:Element(name = "copyright")
    @param:Element(name = "copyright")
    val mCopyright: String? = null,
    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    val mItem: List<PitPassRssItemModel>? = null
)

@Root(name = "item", strict = false)
data class PitPassRssItemModel @JvmOverloads constructor(
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