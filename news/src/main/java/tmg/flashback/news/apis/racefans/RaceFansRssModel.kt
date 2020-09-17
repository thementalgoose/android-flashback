package tmg.flashback.news.apis.racefans

import org.simpleframework.xml.*

const val raceFansDateFormat: String = "EEE, dd MMM yyyy HH:mm:ss Z"

@Root(name = "rss", strict = false)
data class RaceFansRssModel @JvmOverloads constructor(
    @field:Element(name = "channel")
    @param:Element(name = "channel")
    val mChannel: RaceFansRssChannelModel? = null
)

@Root(name = "channel", strict = false)
data class RaceFansRssChannelModel @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val mTitle: String? = null,
    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    val mItem: List<RaceFansRssItemModel>? = null
)

@Root(name = "item", strict = false)
data class RaceFansRssItemModel @JvmOverloads constructor(
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