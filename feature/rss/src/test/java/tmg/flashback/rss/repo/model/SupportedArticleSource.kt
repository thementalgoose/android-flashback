package tmg.flashback.rss.repo.model

fun SupportedArticleSource.Companion.model(
    rssLink: String = "rssLink",
    sourceShort: String = "sourceShort",
    source: String = "source",
    colour: String = "colour",
    textColour: String = "textColour",
    title: String = "title",
    contactLink: String = source
): SupportedArticleSource = SupportedArticleSource(
    rssLink = rssLink,
    sourceShort = sourceShort,
    source = source,
    colour = colour,
    textColour = textColour,
    title = title,
    contactLink = contactLink
)