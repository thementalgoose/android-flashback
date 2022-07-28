package tmg.flashback

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import tmg.flashback.ads.config.di.adsConfigModule
import tmg.flashback.analytics.di.analyticsModule
import tmg.flashback.appshortcuts.di.appShortcutModule
import tmg.flashback.settings.di.settingsModule
import tmg.flashback.configuration.di.configModule
import tmg.flashback.crash_reporting.di.crashReportingModule
import tmg.flashback.debug.di.debugModule
import tmg.flashback.device.di.deviceModule
import tmg.flashback.di.appModule
import tmg.flashback.forceupgrade.di.forceUpgradeModule
import tmg.flashback.notifications.di.notificationModule
import tmg.flashback.releasenotes.di.releaseNotesModule
import tmg.flashback.rss.di.rssModule
import tmg.flashback.stats.di.statsModule
import tmg.flashback.ui.di.uiModule
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.web.di.webBrowserModule

class FlashbackApplication: Application() {

    private val startup: FlashbackStartup by inject()
    private val activityProvider: ActivityProvider by inject()

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
                settingsModule,
                forceUpgradeModule,
                releaseNotesModule,
                webBrowserModule,
                // Debug
                debugModule,
                // Core
                adsConfigModule,
                analyticsModule,
                appShortcutModule,
                configModule,
                crashReportingModule,
                deviceModule,
                notificationModule,
                uiModule
            )
            modules(statsModule)
        }

        // Register application lifecycle callbacks
        registerActivityLifecycleCallbacks(activityProvider)

        // Run startup
        startup.startup(this)
    }
}