package tmg.flashback.firebase.models

class FSupportedSources(
    val sources: List<FSupportedSource>? = null
)

class FSupportedSource(
    val rssLink: String? = null,
    val sourceShort: String? = null,
    val source: String? = null,
    val colour: String? = null,
    val textColour: String? = null,
    val title: String? = null,
    val contactLink: String? = null
)