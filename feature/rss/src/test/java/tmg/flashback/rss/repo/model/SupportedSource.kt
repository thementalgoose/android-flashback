package tmg.flashback.rss.repo.model

fun SupportedSource.Companion.model(
    rssLink: String = "rssLink",
    sourceShort: String = "sourceShort",
    source: String = "source",
    colour: String = "colour",
    textColour: String = "textColour",
    title: String = "title",
    contactLink: String = source
): SupportedSource = SupportedSource(
    rssLink = rssLink,
    sourceShort = sourceShort,
    source = source,
    colour = colour,
    textColour = textColour,
    title = title,
    contactLink = contactLink
)