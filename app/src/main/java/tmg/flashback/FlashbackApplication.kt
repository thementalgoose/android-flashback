package tmg.flashback

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject

@HiltAndroidApp
class FlashbackApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var startup: FlashbackStartup

    @Inject
    lateinit var activityProvider: ActivityProvider

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Register application lifecycle callbacks
        registerActivityLifecycleCallbacks(activityProvider)

        // Run startup
        startup.startup(this)
    }

    override fun getWorkManagerConfiguration() = Configuration
        .Builder()
        .setWorkerFactory(workerFactory)
        .build()
}