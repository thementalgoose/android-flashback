package tmg.flashback

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.flashback.di_old.*

class FlashbackApplication: Application() {

    private val startup: FlashbackStartup by inject()

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@FlashbackApplication)
            modules(
                appModule,
                appRssModule,
                dataModule,
            )
            modules(coreModules)
            modules(sharedModules)
        }

        // Run startup
        startup.startup(this)
    }
}