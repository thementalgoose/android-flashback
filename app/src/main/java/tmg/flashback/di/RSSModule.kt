package tmg.flashback.di

import org.koin.dsl.module
import tmg.flashback.controllers.RSSConfigurationController
import tmg.flashback.managers.networkconnectivity.AndroidNetworkConnectivityManager
import tmg.flashback.managers.sharedprefs.SharedPreferenceManager
import tmg.flashback.rss.controllers.RSSController
import tmg.flashback.rss.managers.RSSNetworkConnectivityManager
import tmg.flashback.rss.network.RSS
import tmg.flashback.rss.prefs.RSSPrefsRepository
import tmg.flashback.rss.repo.RSSRepository

val rssModule = module {

    single<RSSPrefsRepository> { SharedPreferenceManager(get()) }
    single<RSSRepository> { RSS(get(), get()) }
    single<RSSNetworkConnectivityManager> { AndroidNetworkConnectivityManager(get()) }
    single<RSSController> { RSSConfigurationController(get()) }
}