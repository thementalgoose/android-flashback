package tmg.flashback

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.common.di.commonModule
import tmg.configuration.di.configModule
import tmg.core.analytics.di.analyticsModule
import tmg.core.device.di.deviceModule
import tmg.core.ui.di.uiModule
import tmg.crash_reporting.di.crashReportingModule
import tmg.flashback.di.appModule
import tmg.flashback.rss.di.rssModule
import tmg.flashback.statistics.di.statisticsModule
import tmg.flashback.statistics.network.di.networkModule
import tmg.flashback.upnext.di.upNextModule
import tmg.notifications.di.notificationModule

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
                upNextModule,
                commonModule,
                // Core
                analyticsModule,
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