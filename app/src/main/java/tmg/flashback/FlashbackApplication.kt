package tmg.flashback

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import tmg.flashback.ads.di.adsModule
import tmg.flashback.analytics.di.analyticsModule
import tmg.flashback.appshortcuts.di.appShortcutModule
import tmg.flashback.common.di.commonModule
import tmg.flashback.configuration.di.configModule
import tmg.flashback.crash_reporting.di.crashReportingModule
import tmg.flashback.device.di.deviceModule
import tmg.flashback.di.appModule
import tmg.flashback.notifications.di.notificationModule
import tmg.flashback.regulations.di.regulationsModule
import tmg.flashback.rss.di.rssModule
import tmg.flashback.statistics.di.statisticsModule
import tmg.flashback.stats.di.statsModule
import tmg.flashback.ui.di.uiModule

class FlashbackApplication: Application() {

    private val startup: FlashbackStartup by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            workManagerFactory()
            modules(
                appModule,
                // Features
                rssModule,
                commonModule,
                regulationsModule,
                // Debug
                debugModule,
                // Core
                adsModule,
                analyticsModule,
                appShortcutModule,
                configModule,
                crashReportingModule,
                deviceModule,
                notificationModule,
                uiModule
            )
            modules(statisticsModule)
            modules(statsModule)
        }

        // Run startup
        startup.startup(this)
    }
}