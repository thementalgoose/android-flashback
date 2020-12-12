package tmg.flashback.rss.repo.enums

import tmg.flashback.rss.repo.model.ArticleSource
import java.net.MalformedURLException
import java.net.URL

enum class SupportedArticleSource(
        val rssLink: String,
        val sourceShort: String,
        val source: String,
        val colour: String,
        val textColour: String,
        val title: String
) {
    AUTOSPORT(
            rssLink = "https://www.autosport.com/rss/feed/f1",
            sourceShort = "AS",
            source = "https://www.autosport.com",
            colour = "#FF0000",
            textColour = "#ffffff",
            title = "Autosport"
    ),
    CRASHNET(
            rssLink = "https://crash.net/rss/f1",
            sourceShort = "C",
            source = "https://www.crash.net",
            colour = "#E91B1C",
            textColour = "#ffffff",
            title = "Crash.net"
    ),
    MOTORSPORT(
            rssLink = "https://motorsport.com/rss/f1/news/",
            sourceShort = "MS",
            source = "https://www.motorsport.com",
            colour = "#FFD806",
            textColour = "#181818",
            title = "Motorsport"
    ),
    PITPASS(
            rssLink = "https://www.pitpass.com/fes_php/fes_usr_sit_newsfeed.php",
            sourceShort = "PP",
            source = "https://www.pitpass.com",
            colour = "#611818",
            textColour = "#ffffff",
            title = "PitPass"
    ),
    RACEFANS(
            rssLink = "https://racefans.net/feed/",
            sourceShort = "RF",
            source = "https://www.racefans.net",
            colour = "#0077BA",
            textColour = "#ffffff",
            title = "RaceFans"
    );

    companion object {
        fun getByLink(link: String): SupportedArticleSource? {
            try {
                val url = URL(link)
                return values().firstOrNull {
                    val supportUrl = URL(it.rssLink)

                    return@firstOrNull url.host.stripWWW() == supportUrl.host.stripWWW()
                }
            } catch (exception: MalformedURLException) {
                return null
            }
        }

        fun getByRssFeedURL(rssLink: String): SupportedArticleSource? {
            return values().firstOrNull { it.rssLink == rssLink }
        }

        private fun String.stripWWW(): String {
            return when (this.startsWith("www.")) {
                true -> this.substring(4, this.length)
                false -> this
            }
        }
    }


    val article: ArticleSource
        get() {
            return ArticleSource(
                    title = this.title,
                    colour = this.colour,
                    textColor = this.textColour,
                    source = source,
                    shortSource = this.sourceShort,
                    rssLink = this.rssLink
            )
        }
}