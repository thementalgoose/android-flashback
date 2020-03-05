package tmg.f1stats

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tmg.f1stats.di.f1Module

class F1StatsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@F1StatsApplication)
            modules(f1Module)
        }
    }
}