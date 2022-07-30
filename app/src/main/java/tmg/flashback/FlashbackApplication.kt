package tmg.flashback

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import tmg.flashback.ui.navigation.ActivityProvider
import javax.inject.Inject

@HiltAndroidApp
class FlashbackApplication: Application(), Configuration.Provider {

    @Inject
    protected lateinit var startup: FlashbackStartup

    @Inject
    protected lateinit var activityProvider: ActivityProvider

    @Inject
    protected lateinit var workerFactory: HiltWorkerFactory

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