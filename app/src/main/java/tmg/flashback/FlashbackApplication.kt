package tmg.flashback

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import tmg.flashback.ui.navigation.ActivityProvider
import javax.inject.Inject

@HiltAndroidApp
class FlashbackApplication: Application() {

    @Inject
    protected lateinit var startup: FlashbackStartup

    @Inject
    protected lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()

        // Register application lifecycle callbacks
        registerActivityLifecycleCallbacks(activityProvider)

        // Run startup
        startup.startup(this)
    }
}