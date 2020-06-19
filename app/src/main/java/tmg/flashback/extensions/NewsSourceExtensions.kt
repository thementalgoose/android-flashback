package tmg.flashback.extensions

import androidx.annotation.StringRes
import tmg.flashback.R
import tmg.flashback.repo.enums.NewsSource

val NewsSource.title: Int
    get() = when (this) {
        NewsSource.AUTO_SPORT -> R.string.settings_news_sources_autosport_title
        NewsSource.PIT_PASS -> R.string.settings_news_sources_pitpass_title
        NewsSource.CRASH_NET -> R.string.settings_news_sources_crash_net_title
        NewsSource.SKY_SPORTS -> R.string.settings_news_sources_skysports_title
        NewsSource.MOTORSPORT -> R.string.settings_news_sources_motorsport_title
    }

val NewsSource.description: Int
    get() = when (this) {
        NewsSource.AUTO_SPORT -> R.string.settings_news_sources_autosport_description
        NewsSource.PIT_PASS -> R.string.settings_news_sources_pitpass_description
        NewsSource.CRASH_NET -> R.string.settings_news_sources_crash_net_description
        NewsSource.SKY_SPORTS -> R.string.settings_news_sources_skysports_description
        NewsSource.MOTORSPORT -> R.string.settings_news_sources_motorsport_description
    }