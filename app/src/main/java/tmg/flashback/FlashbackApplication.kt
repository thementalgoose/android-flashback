package tmg.flashback

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.ads.di.adsModule
import tmg.flashback.common.di.commonModule
import tmg.flashback.configuration.di.configModule
import tmg.flashback.analytics.di.analyticsModule
import tmg.flashback.appshortcuts.di.appShortcutModule
import tmg.flashback.device.di.deviceModule
import tmg.flashback.ui.di.uiModule
import tmg.flashback.crash_reporting.di.crashReportingModule
import tmg.flashback.di.appModule
import tmg.flashback.rss.di.rssModule
import tmg.flashback.statistics.di.statisticsModule
import tmg.flashback.notifications.di.notificationModule

class FlashbackApplication: Application() {

    private val startup: FlashbackStartup by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            modules(
                appModule,
                // Features
                rssModule,
                commonModule,
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
        }

        // Run startup
        startup.startup(this)
    }
}