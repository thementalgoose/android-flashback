package tmg.flashback.di_old.rss

import org.threeten.bp.LocalDateTime
import tmg.flashback.rss.repo.model.Article
import tmg.flashback.rss.repo.model.ArticleSource

internal val mockRssGoogle: Article = Article(
    id = "google",
    title = "www.google.com",
    description = "Google search engine",
    link = "http://www.google.com",
    date = LocalDateTime.now(),
    source = ArticleSource(
        title = "Google",
        colour = "#000000",
        textColor = "#ffffff",
        rssLink = "https://www.google.com/rss/link",
        source = "https://www.google.com",
        shortSource = "G",
        contactLink = "https://www.google.com"
    )
)