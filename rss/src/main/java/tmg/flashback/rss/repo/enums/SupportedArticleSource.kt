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
        val title: String,
        val contactLink: String = source
) {
    AUTOSPORT(
            rssLink = "https://www.autosport.com/rss/feed/f1",
            sourceShort = "AS",
            source = "https://www.autosport.com",
            colour = "#FF0000",
            textColour = "#ffffff",
            title = "Autosport",
            contactLink = "https://www.autosport.com/contact/"
    ),
    CRASHNET(
            rssLink = "https://crash.net/rss/f1",
            sourceShort = "C",
            source = "https://www.crash.net",
            colour = "#E91B1C",
            textColour = "#ffffff",
            title = "Crash.net",
            contactLink = "https://www.crash.net/contact"
    ),
    MOTORSPORT(
            rssLink = "https://motorsport.com/rss/f1/news/",
            sourceShort = "MS",
            source = "https://www.motorsport.com",
            colour = "#FFD806",
            textColour = "#181818",
            title = "Motorsport",
            contactLink = "https://www.motorsport.com/info/contact/"
    ),
    PITPASS(
            rssLink = "https://www.pitpass.com/fes_php/fes_usr_sit_newsfeed.php",
            sourceShort = "PP",
            source = "https://www.pitpass.com",
            colour = "#611818",
            textColour = "#ffffff",
            title = "PitPass",
            contactLink = "https://www.pitpass.com/contact-us"
    ),
    RACEFANS(
            rssLink = "https://racefans.net/feed/",
            sourceShort = "RF",
            source = "https://www.racefans.net",
            colour = "#0077BA",
            textColour = "#ffffff",
            title = "RaceFans",
            contactLink = "https://www.racefans.net/contact/about-racefans/"
    ),
    F1_FANSITE(
            rssLink = "https://www.f1-fansite.com/feed/",
            sourceShort = "FF",
            source = "https://www.f1-fansite.com",
            colour = "#DF2C19",
            textColour = "#ffffff",
            title = "F1 Fansite",
            contactLink = "https://www.f1-fansite.com/about-us/"
    ),
    BBC_SPORT(
            rssLink = "https://feeds.bbci.co.uk/sport/formula1/rss.xml",
            sourceShort = "BBC",
            source = "https://www.bbc.co.uk",
            colour = "#FFD04C",
            textColour = "#181818",
            title = "BBC Sport",
            contactLink = "https://www.bbc.co.uk/sport/15561348"
    ),
    THEGUARDIAN(
            rssLink = "https://www.theguardian.com/sport/formulaone/rss",
            sourceShort = "TG",
            source = "https://www.theguardian.com",
            colour = "#002B60",
            textColour = "#ffffff",
            title = "The Guardian",
            contactLink = "https://www.theguardian.com/help/contact-us"
    ),
    WTF1(
            rssLink = "https://wtf1.com/feed/",
            sourceShort = "WTF",
            source = "https://wtf1.com",
            colour = "#F66733",
            textColour = "#ffffff",
            title = "WTF1",
            contactLink = "https://wtf1.com/about-us/"
    ),
    GRANDPRIX247(
            rssLink = "https://www.grandprix247.com/feed/",
            sourceShort = "GP",
            source = "https://www.grandprix247.com",
            colour = "#BD0216",
            textColour = "#ffffff",
            title = "GrandPrix247",
            contactLink = "https://www.grandprix247.com/about-gp247/"
    ),
    F1I(
            rssLink = "https://en.f1i.com/news/feed",
            sourceShort = "F1I",
            source = "https://en.f1i.com",
            colour = "#EC192D",
            textColour = "#ffffff",
            title = "F1i",
            contactLink = "https://f1i.com/contact"
    ),
    F1TECHNICAL(
            rssLink = "https://www.f1technical.net/rss/news.xml",
            sourceShort = "FT",
            source = "https://www.f1technical.net/",
            colour = "#2E6A9B",
            textColour = "#ffffff",
            title = "F1Technical",
            contactLink = "https://www.f1technical.net/pr/feedback.php"
    ),
    BEYONDTHEFLAG(
            rssLink = "https://beyondtheflag.com/formula-one/feed/",
            sourceShort = "BTF",
            source = "https://beyondtheflag.com",
            colour = "#01304C",
            textColour = "#ffffff",
            title = "Beyond The Flag",
            contactLink = "https://fansided.com/contact/"
    ),
    GRANDPRIX(
            rssLink = "https://www.grandprix.com/rss.xml",
            sourceShort = "GP",
            source = "https://www.grandprix.com",
            colour = "#D2141E",
            textColour = "#ffffff",
            title = "grandprix"
    );

    companion object {
        fun getByLink(link: String): SupportedArticleSource? {
            return try {
                val url = URL(link)

                values().firstOrNull {
                    val supportUrl = URL(it.rssLink)
                    url.host.stripWWW() == supportUrl.host.stripWWW()
                } ?: findFallback(url)
            } catch (exception: MalformedURLException) {
                null
            }
        }

        private fun findFallback(url: URL): SupportedArticleSource? {
            return supportedFallbackDomains
                    .entries
                    .firstOrNull { it.key == url.host.stripWWW() }
                    ?.value
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

        fun valuesSorted(): List<SupportedArticleSource> {
            return values()
                .sortedBy { it.rssLink
                    .replace("https://www.", "")
                    .replace("http://www.", "")
                    .replace("https://", "")
                    .replace("http://", "")
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
                    rssLink = this.rssLink,
                    contactLink = this.contactLink
            )
        }
}